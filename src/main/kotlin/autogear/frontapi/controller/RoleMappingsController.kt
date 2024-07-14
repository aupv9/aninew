package autogear.frontapi.controller

import autogear.frontapi.exception.NotFoundException
import autogear.frontapi.service.IRoleMappingsService
import autogear.frontapi.service.IRoleService
import common.RoleRepresentation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/api/role-mappings")
@Tag(name = "Role mappings")
class RoleMappingsController(
    val roleMappingsService: IRoleMappingsService,
    val roleService: IRoleService
) {

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.AdminPermission).MANAGER_USER)")
    @PostMapping("/user/{user-id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addUserRoleMappings(@PathVariable("user-id") userID: String,
                            @RequestBody roleRepresentations: List<RoleRepresentation>
    ): ResponseEntity<Any>{

        for (roleRepresentation in roleRepresentations){
            val role = roleService.getRole(roleRepresentation.name)
                ?: throw NotFoundException("Role ${roleRepresentation.name} not found")
            roleMappingsService.addUserRoleMappings(userID, role)
        }
        return ResponseEntity.noContent().build()
    }


    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.AdminPermission).MANAGER_USER)")
    @DeleteMapping("/user/{user-id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteUserRoleMappings(@PathVariable("user-id") userID: String, @RequestBody roleRepresentations: List<RoleRepresentation>): ResponseEntity<Any>{

        for (roleRepresentation in roleRepresentations){
            val role = roleService.getRole(roleRepresentation.name)
                ?: throw NotFoundException("Role ${roleRepresentation.name} not found")
            roleMappingsService.removeUserRoleMappings(userID, role)
        }
        return ResponseEntity.noContent().build()
    }

}