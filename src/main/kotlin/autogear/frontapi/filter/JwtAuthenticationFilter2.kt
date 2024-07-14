package autogear.frontapi.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

class JwtAuthenticationFilter2: AbstractAuthenticationProcessingFilter("/**") {


    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val username = request?.getParameter("username")
        val password = request?.getParameter("password")
        val principal = UsernamePasswordAuthenticationToken(username, password)
        return this.authenticationManager.authenticate(principal)
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        super.successfulAuthentication(request, response, chain, authResult)
        chain?.doFilter(request, response)
    }



    override fun requiresAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Boolean {
        return true
    }

}