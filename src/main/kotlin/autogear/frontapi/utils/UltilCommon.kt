package autogear.frontapi.utils

import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SessionCallback
import java.util.concurrent.TimeUnit

class UtilCommon {


    companion object{

        fun <K : Any?> getKeyWithTransaction(key: K, redisOperations: RedisOperations<String, String>): List<Any>?{
            return redisOperations.execute(object : SessionCallback<List<Any>> {
                override fun <K : Any?, V : Any?> execute(operations: RedisOperations<K, V>): List<Any>  {
                    operations.multi()
                    try {
                        operations.opsForValue().get(key!!)
                        return operations.exec()
                    }catch (e: Exception){
                        operations.discard()
                        throw e
                    }
                }
            })
        }

        fun <K: Any?, V: Any?> setKeyWithTransaction(key: K, value: V, redisOperations: RedisOperations<String, String>, ttl: Long) {
            redisOperations.execute(object : SessionCallback<List<Any>> {
                override fun <K : Any?, V : Any?> execute(operations: RedisOperations<K, V>): List<Any> {
                    operations.multi()
                    try {
                        operations.opsForValue().set((key as K)!!, (value as V)!!, ttl, TimeUnit.MINUTES)
                        return operations.exec() as List<Any>
                    } catch (e: Exception) {
                        operations.discard() // Rollback the transaction
                        throw e
                    }
                }
            })
        }

        fun <K: Any?, V: Any?> deleteKeysTransaction(key: String, redisTemplate: RedisTemplate<K, V>): Boolean {
            return try {
                redisTemplate.execute { connection ->
                    connection.multi()
                    val keys = arrayOf(key)
                    keys.forEach { key ->
                        connection.del(key.toByteArray()) // Convert key to byte array if necessary
                    }
                    connection.exec()
                    true
                } ?: false
            } catch (e: Exception) {
                false
            }
        }
    }
}
