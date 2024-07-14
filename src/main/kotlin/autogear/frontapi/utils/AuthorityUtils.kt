package autogear.frontapi.utils

import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils

class AuthorityUtils {

    companion object {
        fun getRolesFromUserAuthorities(roleHierarchy: RoleHierarchy, authentication: Authentication): Set<String> {
            val userAuthorities = roleHierarchy.getReachableGrantedAuthorities(authentication.authorities)
            return AuthorityUtils.authorityListToSet(userAuthorities)
        }
    }
}