package autogear.frontapi.service

import autogear.frontapi.entity.Permission
import autogear.frontapi.entity.Role
import autogear.frontapi.exception.NotFoundException
import autogear.frontapi.mapper.PermissionDto
import autogear.frontapi.mapper.RoleDto
import autogear.frontapi.mapper.RoleMapper
import autogear.frontapi.repository.PrivilegeRepo
import autogear.frontapi.repository.RoleRepository
import autogear.frontapi.repository.UserRepository
import common.RoleRepresentation
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RoleServiceImp(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val privilegeRepo: PrivilegeRepo,
    private val mapper: RoleMapper
): IRoleService {
    override fun getRoles(): List<RoleDto> {
        return roleRepository.findAll().map {
            val permission = it.permission.map { permission ->
                val permissionEntity = privilegeRepo.findByName(permission.name)
                permissionEntity?.let { PermissionDto(permissionEntity.id, permission.name, permission.description) }
            }
            val roleDto = mapper.toDto(it)

            roleDto.permissions = permission

            roleDto
        }
    }

    override fun getRole(roleName: String): RoleDto? {
        return roleRepository.findByName(roleName)?.let { mapper.toDto(it) }
    }



    override fun addRole(rolePresentation: RoleRepresentation) {
        val roleName = rolePresentation.name
        if(roleName.isBlank()) {
            throw Exception("Role name is empty")
        }
        val attributes = rolePresentation.attributes
        val roleEntity = Role(name = roleName, description = rolePresentation.description, )
        roleRepository.save(roleEntity)
    }

    @Transactional
    override fun updateRole(roleID: String, rolePresentation: RoleRepresentation) {
        val roleEntity = roleRepository.findById(roleID).get()
        if(roleEntity.name != rolePresentation.name) {
            roleEntity.name = rolePresentation.name
        }
        roleEntity.description = rolePresentation.description

        for (permission in rolePresentation.permission!!) {
            val permissionEntity = privilegeRepo.findByName(permission.name)

            if (permissionEntity != null) {
                val currentPermission = roleEntity.permission as LinkedHashSet<Permission>
                // add permission to role
                // check if permission already exists
                currentPermission.add(permissionEntity)
                roleEntity.permission = currentPermission

            }else {
                throw NotFoundException("Permission not found")
            }
        }
        roleRepository.save(roleEntity)
    }

    override fun deleteRole(roleID: String) {
        val roleEntity = roleRepository.findById(roleID).orElseThrow { NotFoundException("Role not found") }
        val users = userRepository.findByRolesNameIn(
            listOf(roleEntity.name)
        )
        if (users.isNotEmpty()) {
            users.forEach {
                it.roles = it.roles?.filter{ role -> role.id != roleID }
            }
        }
        roleRepository.delete(roleEntity)
    }
}