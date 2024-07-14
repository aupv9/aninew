package autogear.frontapi.permission

import org.springframework.security.core.Authentication

interface GroupPermissionEvaluator {
    fun isOwnerGroup(authentication: Authentication, groupID: String): Boolean

    fun requiredManage(authentication: Authentication): Boolean
}