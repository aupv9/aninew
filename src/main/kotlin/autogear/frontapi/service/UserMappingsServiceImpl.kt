package autogear.frontapi.service

import autogear.frontapi.entity.Role
import autogear.frontapi.mapper.RoleDto
import autogear.frontapi.repository.RoleRepository
import autogear.frontapi.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.LinkedList

@Service
class UserMappingsServiceImpl(
    val authenticateRepository: UserRepository,
    val roleRepository: RoleRepository
): IRoleMappingsService {

    override fun addUserRoleMappings(userID: String, role: RoleDto) {
        val userOptional = authenticateRepository.findByUserID(userID)
        if(userOptional.isPresent){
            val user = userOptional.get()
            val roleEntity = role.name?.let { roleRepository.findByName(it) }!!
            if(user.roles!!.isEmpty()){
                val newRoles = LinkedList<Role>()
                newRoles.add(roleEntity)
                user.roles = newRoles
            }else{
                val newRoles = user.roles!!.filter {  it.name != roleEntity.name }.toMutableList()
                newRoles.add(roleEntity)
                user.roles = newRoles.toList()
            }
            authenticateRepository.save(user)
        }

    }

    override fun getUserRoleMappings(userID: String): List<RoleDto> {
        TODO("Not yet implemented")
    }

    override fun removeUserRoleMappings(userID: String, role: RoleDto) {
        val userOptional = authenticateRepository.findByUserID(userID)
        if(userOptional.isPresent){
            val user = userOptional.get()
            val roleEntity = role.name?.let { roleRepository.findByName(it) }!!
            val newRoles = user.roles!!.filter {  it.name != roleEntity.name }.toMutableList()
            user.roles = newRoles.toList()
            authenticateRepository.save(user)
        }
    }
}