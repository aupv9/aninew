package autogear.frontapi.configuration

import autogear.frontapi.service.AuthenticateService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

class UserAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val eventPublisher: ApplicationEventPublisher,
): AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication {
        val token = authentication as UsernamePasswordAuthenticationToken
        val email = token.name
        val user = userDetailsService.loadUserByUsername(email) as AuthenticateService.CustomUserDetails

        // Database Password already encrypted:
        val password = user.password

        val passwordsMatch = passwordEncoder.matches(token.credentials.toString(), password)

        if (!passwordsMatch) {
            eventPublisher.publishEvent(AuthenticationFailureBadCredentialsEvent(authentication, BadCredentialsException("Invalid username/password")))
            throw BadCredentialsException("Invalid username/password")
        }
        return UsernamePasswordAuthenticationToken(
            user, password, user.authorities
        )
    }

    override fun supports(authentication: Class<*>?): Boolean = true

}