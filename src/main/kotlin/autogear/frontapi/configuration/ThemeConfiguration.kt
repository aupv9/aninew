package autogear.frontapi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer



@Configuration
class ThemeEmailConfiguration{

     val EMAIL_TEMPLATE_ENCODING = "UTF-8"
    @Bean
    fun freemarkerConfig(): FreeMarkerConfigurer{
        val freeMarkerConfigurer = FreeMarkerConfigurer()
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates/")
        return freeMarkerConfigurer
    }

    @Bean
     fun emailMessageSource() : ResourceBundleMessageSource {
         val messageSource = ResourceBundleMessageSource();
        messageSource.setBasename("mail/MailMessages");
        return messageSource;
    }

}