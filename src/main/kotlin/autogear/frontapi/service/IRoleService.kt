package autogear.frontapi.service

import autogear.frontapi.mapper.RoleDto
import common.RoleRepresentation

interface IRoleService {
    fun getRoles(): List<RoleDto>
    fun getRole(roleName: String): RoleDto?
    fun addRole(rolePresentation: RoleRepresentation)
    fun updateRole(roleID: String, rolePresentation: RoleRepresentation)
    fun deleteRole(roleID: String)
    
}