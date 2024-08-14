package autogear.frontapi.configuration




import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisSentinelConfiguration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@EnableCaching
class CachingConfiguration (
    private val autoGearConfiguration: AutoGearConfiguration) {

    val log = org.slf4j.LoggerFactory.getLogger(CachingConfiguration::class.java)

    @Bean
    @ConditionalOnProperty(prefix = "auto-gear.redisConfiguration", name = ["type"], havingValue = "alone")
    fun redisConnectionFactoryStandalone(): RedisConnectionFactory {
        val redisConfiguration = autoGearConfiguration.redisConfiguration
        val redisStandaloneConfiguration = RedisStandaloneConfiguration()
        redisStandaloneConfiguration.hostName = redisConfiguration.host
        redisStandaloneConfiguration.port = redisConfiguration.port
        redisStandaloneConfiguration.username = redisConfiguration.userName
        redisStandaloneConfiguration.password = RedisPassword.of(redisConfiguration.password)
        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    @ConditionalOnProperty(prefix = "auto-gear.redisConfiguration", name = ["type"], havingValue = "sentinel")
    fun redisConnectionFactorySentinel(): RedisConnectionFactory {
        val redisConfiguration = autoGearConfiguration.redisConfiguration
        val redisSentinelConfiguration = RedisSentinelConfiguration()
        redisSentinelConfiguration.username = redisConfiguration.userName
        redisSentinelConfiguration.password = RedisPassword.of(redisConfiguration.password)
        return LettuceConnectionFactory(
            redisSentinelConfiguration
        )
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<*, *> {
        val template: RedisTemplate<Any, Any> = RedisTemplate()
        try {
            if(redisConnectionFactory.connection.ping()?.equals("PONG") == true){
                template.connectionFactory =  redisConnectionFactory
                template.keySerializer = StringRedisSerializer()
                template.valueSerializer = StringRedisSerializer()
                template.setEnableTransactionSupport(true)
            }
        } catch (ex: Exception) {
            log.info("Can't connect to server Redis: ", ex)
            template.connectionFactory =  redisConnectionFactory
        }

        return template
    }



    companion object{
        val setCacheForAuthenticate = setOf("AT", "RT")
    }


}