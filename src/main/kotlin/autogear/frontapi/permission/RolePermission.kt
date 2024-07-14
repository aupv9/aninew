package autogear.frontapi.permission

import autogear.frontapi.enum.BasePermission
import autogear.frontapi.repository.RoleRepository
import autogear.frontapi.utils.AuthorityUtils
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class RolePermission(
    val roleRepository: RoleRepository,
    val roleHierarchy: RoleHierarchy
): RoleEvaluator {
    override fun hasPermission(authentication: Authentication, basePermissions: BasePermission): Boolean {
        val roles = AuthorityUtils.getRolesFromUserAuthorities(roleHierarchy, authentication)

        return false
    }
}