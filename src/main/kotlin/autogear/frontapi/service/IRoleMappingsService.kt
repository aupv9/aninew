package autogear.frontapi.service

import autogear.frontapi.mapper.RoleDto

interface IRoleMappingsService {
    fun addUserRoleMappings(userID: String, role: RoleDto)

    fun getUserRoleMappings(userID: String): List<RoleDto>
    fun removeUserRoleMappings(userID: String, role: RoleDto)
}