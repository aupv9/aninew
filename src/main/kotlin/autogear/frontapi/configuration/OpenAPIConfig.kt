package autogear.frontapi.configuration

import io.swagger.v3.oas.annotations.security.SecuritySchemes
import io.swagger.v3.oas.models.OpenAPI
import jakarta.servlet.http.HttpServletRequest
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextListener
import org.springframework.web.filter.RequestContextFilter

@Configuration
@SecuritySchemes(

)
class OpenAPIConfig(
    val autoGearConfiguration: AutoGearConfiguration,
    val httpServletRequest: HttpServletRequest
) {

    @Bean
    fun  openAPI(): OpenAPI {
        val openAPI  = OpenAPI()

        val info =  io.swagger.v3.oas.models.info.Info()
        info.title = "AutoGear API"
        info.version = "1.0.0"
        info.description = "AutoGear API"

        openAPI.info = info

        val server =  io.swagger.v3.oas.models.servers.Server()

       // server.url = httpServletRequest.scheme + "://" + httpServletRequest.serverName + ":" + httpServletRequest.serverPort
        server.url = "http://localhost:8001"
        openAPI.servers = listOf(server)


        return openAPI
    }



//    @Bean
//    fun openAPI(): OpenAPI {
//        return OpenAPI().info(
//            io.swagger.v3.oas.models.info.Info()
//                .title("AutoGear API")
//                .version("1.0.0").contact(io.swagger.v3.oas.models.info.Contact().name("AutoGear"))
//                .description("AutoGear API")
//            )
//            .servers(
//            listOf(
//                io.swagger.v3.oas.models.servers.Server().url(
//                    httpServletRequest.scheme + "://" + httpServletRequest.serverName + ":" + httpServletRequest.serverPort
//                )
//            )
//        )
//    }

    @Bean
    fun requestContextListener(): RequestContextListener = RequestContextListener()

    @Bean
    fun requestContextFilter(): FilterRegistrationBean<RequestContextFilter>{
        val filter = FilterRegistrationBean<RequestContextFilter>()
        filter.order = 1
        filter.filter = RequestContextFilter()
        return filter
    }
}