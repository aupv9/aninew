package autogear.frontapi.controller

import autogear.frontapi.enum.Role
import autogear.frontapi.service.IUserService
import autogear.frontapi.service.RoleServiceImp
import autogear.frontapi.service.authen.AuthenticationService
import common.CommonResponse
import common.PasswordRequest
import common.UserPermission
import common.WhoAmI
import org.springframework.context.MessageSource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

import java.util.*

@RestController
@RequestMapping("/v1/api/users")
class UserController(
    private val userService: IUserService,
    private val authenticationService: AuthenticationService,
    private val messageSource: MessageSource,
    private val roleService: RoleServiceImp
){

    private fun getMessage(key: String, default: String? = null): String? = messageSource.getMessage("user.action.$key", null, default, Locale.ENGLISH )


    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.UserMemberPermission).MANAGE_PROFILE_USER) " +
            "&& authentication.principal == #userID")
    @PatchMapping("/password/{userID}")
    fun updatePassword(@RequestBody(required = false) request: PasswordRequest, @PathVariable("userID") userID: String): ResponseEntity<CommonResponse<Nothing>> {
        val result = authenticationService.changePassword(request)
        val responseContent = CommonResponse(data = null, status = result, message = if(result) getMessage("change-password-success", "Change password success")
        else getMessage("change-password-fail", "Change password Fail"), code = if(result) 200 else 666)
        return ResponseEntity.ok(responseContent)
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.AdminPermission).MANAGER_USER)")
    @GetMapping("/{appName}/whoami")
    fun whoAmI(@PathVariable appName: String): ResponseEntity<CommonResponse<WhoAmI>> {
        val userID = SecurityContextHolder.getContext().authentication.name
        val whoAmI = WhoAmI(userID = userID)
        val userOptional = userService.findUserById(userID)
        if(userOptional.isPresent){
            val user = userOptional.get()
            whoAmI.displayName = user.name
            whoAmI.isManageApp = user.roles?.any { it.name == Role.ROLE_ADMIN.name } ?: false
            val role  = user.roles?.find { it.name == Role.ROLE_ADMIN.name }?.name?.let { roleService.getRole(it) }
            val appAccess = mutableMapOf(
                appName to mutableSetOf<String>()
            )
            role?.permissions?.map {
                val item = appAccess[appName] as MutableSet
                if (it != null) {
                    item.add(it.name)
                }
            }
            whoAmI.appAccess = appAccess
        }

        return ResponseEntity.ok(
            CommonResponse(
                data = whoAmI,
                status = true,
                message = "WhoAmI success",
                code = 200
            )
        )
    }


    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.AdminPermission).MANAGER_USER)")
    @GetMapping(
        value = ["/profile"],
        produces = [MediaType.APPLICATION_JSON_VALUE],)
    fun getProfile(): ResponseEntity<CommonResponse<Any>> {
        val userID = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok(CommonResponse(data = null, status = true, message = "Get profile success"))
    }
}