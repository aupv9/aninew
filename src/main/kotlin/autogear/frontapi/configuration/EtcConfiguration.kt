package autogear.frontapi.configuration

import autogear.frontapi.listener.SpringSecurityAuditorAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware

@Configuration
class EtcConfiguration {

    @Bean
    fun auditorAware(): AuditorAware<String> = SpringSecurityAuditorAware()
}