package autogear.frontapi.permission

import autogear.frontapi.enum.BasePermission
import org.springframework.security.core.Authentication

interface RoleEvaluator {
    fun hasPermission(authentication: Authentication, basePermissions: BasePermission): Boolean

}