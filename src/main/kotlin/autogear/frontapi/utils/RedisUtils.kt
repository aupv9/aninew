package autogear.frontapi.utils

import org.springframework.data.redis.connection.RedisConnectionFactory

class RedisUtils {
    companion object {
        fun checkConnection(redisConnectionFactory: RedisConnectionFactory): Boolean {
            return try {
                redisConnectionFactory.connection.ping() == "PONG"
            } catch (ex: Exception) {
                false
            }
        }
    }
}