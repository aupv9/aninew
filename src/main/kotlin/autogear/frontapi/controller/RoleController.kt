package autogear.frontapi.controller

import autogear.frontapi.service.IRoleService
import common.CommonResponse
import common.RoleRepresentation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/api/roles")
@Tag(name = "Role access")
class RoleController(
    val roleService: IRoleService
) {

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.AdminPermission).CREATE_NEW_ROLE)")
    @PostMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createNewRole(@RequestBody roleRepresentation: RoleRepresentation): ResponseEntity<Any>{
        roleService.addRole(roleRepresentation)
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.AdminPermission).MANAGER_ROLE)")
    @PutMapping("/{role-id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRole(@PathVariable("role-id") roleID: String, @RequestBody roleRepresentation: RoleRepresentation): ResponseEntity<Any> {
        roleService.updateRole(roleID, roleRepresentation)
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.AdminPermission).MANAGER_ROLE)")
    @GetMapping("/{role-id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRole(@PathVariable("role-id") roleID: String): ResponseEntity<Any> {
        val roleRepresentation = roleService.getRole(roleID)
        val response = CommonResponse(
            data = roleRepresentation,
            message = "Role found", status = true, code = 200
        )
        return ResponseEntity.ok(
            response
        )
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.AdminPermission).MANAGER_ROLE)")
    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRoles(): ResponseEntity<Any> {
        val roleRepresentation = roleService.getRoles()
        val response = CommonResponse(
                data = roleRepresentation,
            message = "Roles found", status = true, code = 200
        )
        return ResponseEntity.ok(
            response
        )
    }

        @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication,T(autogear.frontapi.enum.AdminPermission).MANAGER_ROLE)")
    @PostMapping("/{role-id}/delete", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteRole(@PathVariable("role-id") roleID: String): ResponseEntity<Any> {
        roleService.deleteRole(roleID)
        return ResponseEntity.noContent().build()
    }
}