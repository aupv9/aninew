package autogear.frontapi.configuration.cache

import org.springframework.data.redis.cache.CacheStatistics
import org.springframework.data.redis.cache.CacheStatisticsCollector
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import java.time.Duration

class CacheWriterForAuthenticate(
    val cacheConnectionFactory: RedisConnectionFactory
): RedisCacheWriter {


    override fun getCacheStatistics(cacheName: String): CacheStatistics {
        TODO("Not yet implemented")
    }

    override fun put(name: String, key: ByteArray, value: ByteArray, ttl: Duration?) {
        TODO("Not yet implemented")
    }

    override fun get(name: String, key: ByteArray): ByteArray? {
        TODO("Not yet implemented")
    }

    override fun putIfAbsent(name: String, key: ByteArray, value: ByteArray, ttl: Duration?): ByteArray? {
        TODO("Not yet implemented")
    }

    override fun remove(name: String, key: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun clean(name: String, pattern: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun clearStatistics(name: String) {
        TODO("Not yet implemented")
    }

    override fun withStatisticsCollector(cacheStatisticsCollector: CacheStatisticsCollector): RedisCacheWriter {
        TODO("Not yet implemented")
    }
}