package autogear.frontapi.listener

import org.springframework.data.domain.AuditorAware
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class SpringSecurityAuditorAware: AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter{
                it !is AnonymousAuthenticationToken
            }.map{
                return@map it.principal as String
            }
            .map(String::class.java::cast)
    }

}