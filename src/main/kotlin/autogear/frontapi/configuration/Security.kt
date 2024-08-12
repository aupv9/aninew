package autogear.frontapi.configuration

import autogear.frontapi.filter.JwtAuthenticationFilter
import autogear.frontapi.service.AuthenticateService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.*
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true
)
class SecurityConfiguration(val userDetailsService: AuthenticateService,
                            val configuration: AutoGearConfiguration,
                            val eventPublisher: ApplicationEventPublisher,
                            val jwtAuthenticationFilter: JwtAuthenticationFilter){


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val version = configuration.api.version
        http.authorizeHttpRequests {
            it.requestMatchers("/swagger-ui.html" , "/swagger-ui/**", "/v3/api-docs/**" , "/webjars/**"
                , "/swagger-resources/**" ,"/public/**").permitAll()
            it.requestMatchers( "/$version/api/auth/**").permitAll()
            it.anyRequest().authenticated()
        }

        http.formLogin { form -> form.disable() }
        http.httpBasic { basic -> basic.disable() }
        http.csrf { it.disable() }


        http.sessionManagement { session ->
            session.sessionFixation().none()
            session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
        }


        http.cors{
            it.configurationSource(corsConfigurationSource())
        }

        http.exceptionHandling {
            it.accessDeniedHandler { _, response, _ ->
                response.status = 403
                response.writer.write("Access Denied")
            }

            it.authenticationEntryPoint { _, response, exception ->
                response.status = 401
                when(exception){
                    is LockedException ->  exception.message?.let { it1 -> response.writer.write(it1) }
                    else ->  response.writer.write("Authentication error: Required authentication credentials were not provided")
                }
            }
        }


        http.authenticationManager(authenticationManager(userDetailsService, passwordEncoder(), eventPublisher))

        http.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
//      http.addFilterBefore(IOAuthenticateFilter(), UsernamePasswordAuthenticationFilter::class.java)


        return http.build()
    }

    @Bean
    fun authenticationManager(
        userDetailsService: UserDetailsService, passwordEncoder: PasswordEncoder, eventPublisher: ApplicationEventPublisher): AuthenticationManager {
        val authenticationProvider = UserAuthenticationProvider(userDetailsService, passwordEncoder, eventPublisher)
        return ProviderManager(authenticationProvider)
    }

    @Bean
    fun authenticationProvider(userDetailsService: UserDetailsService, passwordEncoder: PasswordEncoder, eventPublisher: ApplicationEventPublisher): AuthenticationProvider{
        return UserAuthenticationProvider(userDetailsService, passwordEncoder, eventPublisher)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(4)


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders =  listOf("*")
        configuration.allowCredentials = true
        configuration.exposedHeaders = listOf("*")
        configuration.allowedOriginPatterns = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy{
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy(configuration.security.roleHierarchyHierarchy)
        return roleHierarchy
    }

    @Bean
    fun methodSecurityExpressionHandler(roleHierarchy: RoleHierarchy): MethodSecurityExpressionHandler{
        val expressionHandler = DefaultMethodSecurityExpressionHandler()
        expressionHandler.setRoleHierarchy(roleHierarchy)
        return expressionHandler
    }
}




