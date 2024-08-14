package autogear.frontapi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import java.util.concurrent.Executors

@Configuration
class SchedulerConfig {

    @Bean
    fun taskScheduler(): ConcurrentTaskScheduler {
        return ConcurrentTaskScheduler(
            Executors.newScheduledThreadPool(100)
        )
    }
}