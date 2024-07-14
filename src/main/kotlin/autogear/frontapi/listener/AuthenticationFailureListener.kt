package autogear.frontapi.listener

import autogear.frontapi.service.authen.ILoginAttemptService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.stereotype.Component

@Component
class AuthenticationFailureListener(
    val loginAttemptService: ILoginAttemptService?,
    val httpRequestServletRequest: HttpServletRequest?
): ApplicationListener<AuthenticationFailureBadCredentialsEvent> {


    override fun onApplicationEvent(event: AuthenticationFailureBadCredentialsEvent) {
        val xfHeader = httpRequestServletRequest?.getHeader("X-Forwarded-For")
        if(xfHeader.isNullOrEmpty() || !httpRequestServletRequest?.let { xfHeader.contains(it.remoteAddr) }!!)
            httpRequestServletRequest?.remoteAddr?.let { loginAttemptService?.loginFailed(it) }
        else
            loginAttemptService?.loginFailed(xfHeader.split(",").first())
    }
}