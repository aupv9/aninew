package autogear.frontapi.filter


import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.entity.TypeKey
import autogear.frontapi.exception.JwtInvalidException
import autogear.frontapi.repository.KeysRepository
import autogear.frontapi.repository.UserRepository
import autogear.frontapi.service.AuthenticateService
import autogear.frontapi.utils.RedisUtils
import autogear.frontapi.utils.UtilCommon
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import common.Utils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.security.InvalidKeyException
import java.security.interfaces.RSAPublicKey
import java.util.*


@Component
class JwtAuthenticationFilter(
    val authenticateRepository: UserRepository,
    val keysRepository: KeysRepository, 
    val userDetailsService: UserDetailsService,
    val redisTemplate: RedisTemplate<String, String>,
    val autoGearConfiguration: AutoGearConfiguration
): OncePerRequestFilter(), Cacheable {
    val logger: Log = LogFactory.getLog(JwtAuthenticationFilter::class.java)

    companion object{
        const val STRING_EMPTY: String = ""
    }

    override fun checkCache(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleWithCache(key: String) {
        TODO("Not yet implemented")
    }


    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if(request.cookies != null){
            val cookieAT = request.cookies.find{ it.name == Utils.USER_SESSION }
            if (cookieAT != null && cookieAT.value.isNotBlank()) {
                try {
                    val body = Utils.parseToken(cookieAT.value)
                    val userID = body["userID"]
                    var accessToken = STRING_EMPTY
                    var accessTokenBucket = Collections.emptyList<Any>()
                    if(RedisUtils.checkConnection(redisTemplate.connectionFactory!!)){
                        val resultAfterGet = UtilCommon.getKeyWithTransaction("AT_$userID", redisTemplate)
                        if(resultAfterGet?.isEmpty() == true){
                            accessTokenBucket = resultAfterGet
                        }
                        if (accessTokenBucket.isEmpty()){
                            var refreshToken = STRING_EMPTY
                            keysRepository.findByUserAndType(userID = userID as String, TypeKey.ACCESS).ifPresent {
                                accessToken = it.accessToken!!
                                refreshToken = it.refreshToken[0]
                            }
                            if (accessToken.isNotBlank()){
                                UtilCommon.setKeyWithTransaction("AT_$userID", accessToken, redisTemplate, autoGearConfiguration.redisConfiguration.cacheWithTTL["AT"]!!)
                                UtilCommon.setKeyWithTransaction("RT_$userID", refreshToken, redisTemplate, autoGearConfiguration.redisConfiguration.cacheWithTTL["RT"]!!)
                            }
                        }
                    }else{
                        keysRepository.findByUserAndType(userID = userID as String, TypeKey.ACCESS).ifPresent {
                            accessToken = it.accessToken!!
                        }
                    }

                    if (accessToken.isEmpty() && accessTokenBucket.isEmpty()) filterChain.doFilter(request, response)
                    if (accessTokenBucket.isNotEmpty()) {
                        if (accessToken != accessTokenBucket[0]) filterChain.doFilter(request, response)
                    }
                    authenticateRepository.findByUserID(userID as String).ifPresent {
                            user -> run {
                                keysRepository.findByUser(user = userID).ifPresent { keys -> run {
                                    try {
                                        val publicKeyStr = keys.publicKey
                                        val publicKey = Utils.getPublicKeyFromString(publicKeyStr)
                                        val algorithm = Algorithm.RSA256(publicKey as RSAPublicKey?, null)
                                        if (Utils.verifyToken(algorithm, cookieAT.value)) {
                                            val securityContext = SecurityContextHolder.getContext()
                                            val userDetails = userDetailsService.loadUserByUsername(user.email) as AuthenticateService.CustomUserDetails
                                            securityContext.authentication = UsernamePasswordAuthenticationToken(userDetails.getUserId(), user.password, userDetails.authorities)
                                        }else{
                                            throw JwtInvalidException("Token verify fail")
                                        }
                                    }catch (e: InvalidKeyException){
                                        throw BadCredentialsException("Invalid Token")
                                    }catch (e: IOException){
                                        throw BadCredentialsException("Invalid Token")
                                    }
                                }
                            }
                        }
                    }
                }catch (e: JWTDecodeException){
                    logger.debug("JWT invalid format or not decode")
                    throw AccessDeniedException("JWT invalid format or not decode")
                }
            }
        }
        filterChain.doFilter(request, response)
    }


}
