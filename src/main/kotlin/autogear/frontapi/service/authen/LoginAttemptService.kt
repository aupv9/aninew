package autogear.frontapi.service.authen

import autogear.frontapi.configuration.AutoGearConfiguration
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class LoginAttemptService(
    var autoGearConfiguration: AutoGearConfiguration,
    var httpServletRequest: HttpServletRequest
): ILoginAttemptService {

    companion object{
        const val MAX_ATTEMPT: Int = 10
        lateinit var attemptCache: LoadingCache<String, Int>
    }

    init {
        attemptCache = CacheBuilder.newBuilder().expireAfterWrite(autoGearConfiguration.security.TTLLoggedIn, TimeUnit.DAYS)
            .build(
                object : CacheLoader<String, Int>() {
                    override fun load(key: String): Int {
                        return 0
                    }
                }
            )
    }


    override fun isBlocked(): Boolean {
        return try {
            attemptCache.get(getIp()) >= autoGearConfiguration.security.MaxAttempt
        }catch (e: Exception){
            false
        }
    }

    override fun loginFailed(key: String) {
        attemptCache.asMap().forEach { (t, u) -> println("$t:$u") }
        var attempt: Int = try {
            attemptCache.get(key)
        }catch (e: Exception){
            0
        }
        attempt++
        attemptCache.put(key, attempt)
    }


    private fun getIp():  String {
        val xfHeader = httpServletRequest.getHeader("X-Forwarded-For")
        if(xfHeader != null) return xfHeader.split(",").first()
        return  httpServletRequest.remoteAddr
    }
}
