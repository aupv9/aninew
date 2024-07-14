package autogear.frontapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication(
	exclude = [
		RedisAutoConfiguration::class, SecurityAutoConfiguration::class
	], scanBasePackages = ["autogear.frontapi"]
)
@EnableConfigurationProperties
@EnableMongoAuditing(
	auditorAwareRef = "auditorAware"
)
@EnableAsync
class FrontApiApplication

fun main(args: Array<String>) {
	runApplication<FrontApiApplication>(*args)
}
