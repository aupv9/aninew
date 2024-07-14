package autogear.frontapi.permission

import autogear.frontapi.exception.NotFoundException
import autogear.frontapi.repository.GroupRepository
import autogear.frontapi.repository.RoleRepository
import autogear.frontapi.repository.UserRepository
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class GroupPermissions(
    val roleHierarchy: RoleHierarchy,
    val roleRepository: RoleRepository,
    val authenticateRepository: UserRepository,
    val groupRepository: GroupRepository
): GroupPermissionEvaluator {


    override fun isOwnerGroup(authentication: Authentication, groupID: String): Boolean {
        val groupOptional = groupRepository.findById(groupID)
        if(!groupOptional.isPresent){
            throw NotFoundException("Not Found Group")
        }
        val userID = authentication.principal as String
        return groupRepository.findByIdAndOwnerUserID(groupID, userID).isPresent
    }

    override fun requiredManage(authentication: Authentication): Boolean {

        TODO("Not yet implemented")
    }

}
