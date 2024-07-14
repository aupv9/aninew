package autogear.frontapi.exception

import org.springframework.security.core.AuthenticationException

class JwtInvalidException(override var message: String? = "Jwt is invalid") : AuthenticationException(message)
