package autogear.frontapi.filter

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class CacheProvider(
    val redisTemplate: RedisTemplate<String, String>
    ): Cacheable {
    override fun checkCache(key: String): Boolean {
        TODO("Not yet implemented")
    }


    override fun handleWithCache(key: String) {
        TODO("Not yet implemented")
    }
}