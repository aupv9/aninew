package autogear.frontapi.permission

import autogear.frontapi.enum.BasePermission
import autogear.frontapi.repository.RoleRepository
import autogear.frontapi.utils.AuthorityUtils
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class UserPermissionEvaluator(
    val roleHierarchy: RoleHierarchy,
    val roleRepository: RoleRepository,
): PermissionEvaluator {

    override fun hasPermission(authentication: Authentication, vararg permissions: BasePermission): Boolean {
        val roles = AuthorityUtils.getRolesFromUserAuthorities(roleHierarchy, authentication)
        for(role in roles){
            if(roleRepository.findByName(role)?.permission?.find {
                    permissions.find { permission -> it.name == permission.getName() } != null
                } != null) return true
        }
        return false
    }

    fun isOwner(authentication: Authentication): Boolean{
        return false
    }
}