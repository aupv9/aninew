package autogear.frontapi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource


@Configuration
class MessageSourceConfig {


    @Bean
    fun messageSource(): ResourceBundleMessageSource {
        val resourceBundleMessageSource  = ResourceBundleMessageSource()
        resourceBundleMessageSource.setBasenames("messages")
        resourceBundleMessageSource.setDefaultEncoding("UTF-8")
        resourceBundleMessageSource.setFallbackToSystemLocale(false);
        return resourceBundleMessageSource
    }

}