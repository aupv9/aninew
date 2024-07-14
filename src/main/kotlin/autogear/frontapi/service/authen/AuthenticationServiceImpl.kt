package autogear.frontapi.service.authen

import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.entity.Keys
import autogear.frontapi.entity.TypeKey
import autogear.frontapi.entity.User
import autogear.frontapi.filter.JwtAuthenticationFilter
import autogear.frontapi.repository.KeysRepository
import autogear.frontapi.repository.UserRepository
import autogear.frontapi.service.AuthenticateService
import autogear.frontapi.utils.RedisUtils
import autogear.frontapi.utils.UtilCommon
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import common.*
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.interfaces.RSAPublicKey

import java.util.*
import java.util.concurrent.TimeUnit


@Service
class AuthenticationServiceImpl(
    val authenticateRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager,
    val eventPublisher: ApplicationEventPublisher,
    val autoGearConfiguration: AutoGearConfiguration,
    val keysRepository: KeysRepository,
    val redisTemplate: RedisTemplate<String, String>,
    val redisConnectionFactory: RedisConnectionFactory
) : AuthenticationService {

    private final val logger: Log = LogFactory.getLog(JwtAuthenticationFilter::class.java)


    override fun login(username: String, password: String): ResponseAuthenticate {
        val authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password)
        val authenticationResponse = authenticationManager.authenticate(authenticationRequest)
        if(authenticationResponse.isAuthenticated){
            val user = authenticationResponse.principal as AuthenticateService.CustomUserDetails
            return this.createSession(user.getUserId(), user.getEmail())
        }
        return ResponseAuthenticate("", "", false)
    }

    private fun createSessionInternal(userID: String, email: String, expirationDateRT: Date): ResponseAuthenticate{

        val keyPairGenerator = Utils.generateKeyPair()
        val expirationDateAT = Date(System.currentTimeMillis() * 1000 + autoGearConfiguration.security.expiryAT)

        val claims = mapOf(
            "userID" to userID,
            "email" to email
        )
        return try {
            val accessToken = Utils.generateToken(claims , expirationDateAT , keyPairGenerator.private)
            val refreshToken = Utils.generateToken(claims , expirationDateRT , keyPairGenerator.private)

            val mapTTL = autoGearConfiguration.redisConfiguration.cacheWithTTL
            if(RedisUtils.checkConnection(redisTemplate.connectionFactory!!)){
                UtilCommon.setKeyWithTransaction("AT_$userID", accessToken, redisTemplate, mapTTL["AT"]!!)
                UtilCommon.setKeyWithTransaction("RT_$userID", refreshToken, redisTemplate, mapTTL["RT"]!!)
            }


            val publicKeyStr = Utils.getPublicKey(keyPairGenerator)
            keysRepository.findByUser(userID).ifPresentOrElse({
                it.publicKey = publicKeyStr
                it.refreshToken = listOf(refreshToken)
                it.accessToken = accessToken
                keysRepository.save(it)
            }, {
                val keys =  Keys(userID, Utils.getPublicKey(keyPairGenerator), listOf(refreshToken), accessToken = accessToken, type = TypeKey.ACCESS)
                keysRepository.save(keys)
            })
            ResponseAuthenticate(accessToken, refreshToken, true)
        }catch (_: Exception){
            ResponseAuthenticate("", "", false)
        }
    }

     private inline fun <reified T> setValueStringWithTTLAndPrefix(key: String, value: T,  prefix: String, ttl: Long){
         val prefixedKey = prefix + "_" + key
         val ops = redisTemplate.opsForValue()
         ops.set(prefixedKey, value!! as String)
         redisTemplate.expire(prefixedKey , ttl , TimeUnit.MINUTES)
    }

    private inline fun <reified T> getValueString(key: String): Any? = redisTemplate.opsForValue()[key]

    companion object{
        const val STRING_EMPTY = ""
    }
    override fun logout(accessToken: String, refreshToken: String): Boolean {
        val securityContext = SecurityContextHolder.getContext()
        val user = securityContext.authentication as UsernamePasswordAuthenticationToken
        val userID = user.principal as String

        var isSuccess = false
        if(RedisUtils.checkConnection(redisTemplate.connectionFactory!!)){
            val acTokenBucket = UtilCommon.getKeyWithTransaction("AT_$userID", redisTemplate)
            val refreshTokenBucket = UtilCommon.getKeyWithTransaction("RT_$userID", redisTemplate)
            val isVisible = (accessToken == acTokenBucket?.get(0) as String &&
                    refreshToken == refreshTokenBucket?.get(0) as String)
            if(isVisible){
                isSuccess =  UtilCommon.deleteKeysTransaction("AT_$userID", redisTemplate)
                        && UtilCommon.deleteKeysTransaction("RT_$userID", redisTemplate)
            }
        }
        val keysOptional = keysRepository.findByUserAndType(userID, TypeKey.ACCESS)
        if(keysOptional.isPresent){
            val keys = keysOptional.get()
            keys.accessToken = STRING_EMPTY
            keys.refreshToken = Collections.emptyList()
            keysRepository.save(keys)
            isSuccess = true
        }
        return isSuccess
    }

    override fun isLogin(token: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun signUp(signUpRequest: RegisterRequest): User {
        val user = User(email = signUpRequest.email, password = passwordEncoder.encode(signUpRequest.password), userID = Utils.generateId(), enabled = false)
        return authenticateRepository.save(user)
    }

    override fun isExited(email: String) = authenticateRepository.findByEmail(email).isPresent
    override fun createSession(userID: String, email: String): ResponseAuthenticate = this.createSessionInternal(userID, email,
                                expirationDateRT = Date(System.currentTimeMillis() * 1000 + autoGearConfiguration.security.expiryRT))


    override fun newTokenFromRefresh(refreshToken: String): ResponseAuthenticate {
        try {
            val body = Utils.parseToken(refreshToken)
            val userID = body["userID"] as String
            val expiryTime = Utils.getExpiryTime(refreshToken)

            val keys = keysRepository.findByUser(userID).get()
            val algorithm = Algorithm.RSA256(Utils.getPublicKeyFromString(keys.publicKey) as RSAPublicKey?, null )

            if(RedisUtils.checkConnection(redisTemplate.connectionFactory!!)){
                val refreshTokenBucket = UtilCommon.getKeyWithTransaction("RT_$userID", redisTemplate)
                if(refreshTokenBucket != null){
                    if(refreshTokenBucket.isNotEmpty()) {
                        if (refreshTokenBucket[0] == refreshToken && Utils.verifyToken(algorithm, refreshToken)) {
                            return this.createSessionInternal(userID, body["email"] as String, expiryTime)
                        }
                    }else return this.getAndUpdateToCache(algorithm, refreshToken, userID, email = body["email"] as String, expiryTime)
                }
            }else return this.getAndUpdateToCache(algorithm, refreshToken, userID, email = body["email"] as String, expiryTime)

        }catch (e: JWTDecodeException){
            logger.debug("JWT invalid format or not decode")
            throw AccessDeniedException("JWT invalid format or not decode")
        }
        return ResponseAuthenticate("", "", false)
    }

    private fun getAndUpdateToCache(algorithm: Algorithm, refreshToken: String, userID: String, email: String, expiryTime: Date): ResponseAuthenticate{
        val keysOptional = keysRepository.findByUserAndType(userID, TypeKey.ACCESS)
        if(keysOptional.isPresent){
            if(keysOptional.get().refreshToken.contains(refreshToken) && Utils.verifyToken(algorithm, refreshToken)) {
                return this.createSessionInternal(userID, email , expiryTime)
            }
        }
        return ResponseAuthenticate("", "", false)
    }

    override fun changePassword(request: PasswordRequest): Boolean {
        val contextHolder = SecurityContextHolder.getContext()
        val user = contextHolder.authentication as UsernamePasswordAuthenticationToken
        val userID = user.principal as String
        val userDB = authenticateRepository.findByUserID(userID).get()
        if (passwordEncoder.matches(request.oldPassword, userDB.password)) {
            userDB.password = passwordEncoder.encode(request.password)
            authenticateRepository.save(userDB)
            return true
        }
        return false
    }

    override fun resetPassword(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun havePermission(userID: String, permission: String): Boolean {
        TODO("Not yet implemented")
    }

}