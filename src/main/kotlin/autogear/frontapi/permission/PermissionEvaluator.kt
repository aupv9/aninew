package autogear.frontapi.permission

import autogear.frontapi.enum.BasePermission
import org.springframework.security.core.Authentication

interface PermissionEvaluator {
    fun hasPermission(authentication: Authentication, vararg permissions: BasePermission): Boolean
}
