package autogear.frontapi.service

import autogear.frontapi.entity.Group
import autogear.frontapi.entity.User
import autogear.frontapi.exception.NotFoundException
import autogear.frontapi.repository.GroupRepository
import autogear.frontapi.repository.UserRepository
import common.GroupRepresentation
import common.UserRepresentation
import common.Utils
import org.springframework.data.domain.PageRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.stream.Stream

@Service
class GroupService(
    val groupRepository: GroupRepository,
    val authenticateRepository: UserRepository
): IGroupService {

    override fun createNewGroup(groupRep: GroupRepresentation): Boolean {
        if(groupRep.parentId.isNullOrBlank()){

        }else{
            val context = SecurityContextHolder.getContext()
            val authentication = context.authentication as UsernamePasswordAuthenticationToken
            authenticateRepository.findByUserID(authentication.principal as String).ifPresent {
                val group = Group(id = Utils.generateId(), name = groupRep.name, owner = it)
                if(groupRep.parentId != null){
                    val parent = groupRepository.findById(groupRep.parentId!!).get()
                    group.parent = parent
                }
                groupRepository.save(group)
            }
        }
        return true
    }

    override fun getGroupById(id: String): Group?{
        val groupOptional = this.groupRepository.findById(id)
        return if(groupOptional.isPresent){
            val userID =  SecurityContextHolder.getContext().authentication.principal as String
            if(groupOptional.get().owner.userID != userID) return null
            groupOptional.get()
        }else{
            null
        }
    }

    override fun moveGroup(group: Group, toParent: Group?) {
        if (toParent != null) {
            if(group.parent?.id == toParent.id) return
        }

        if(group.parent?.id != null){
            group.parent?.removeChild(group)
        }
        group.parent = toParent

        if(toParent != null) toParent.addChild(group)
        else group.parent = null
    }


    override fun getChildrenByGroup(groupID: String, size: Int, page: Int): List<GroupRepresentation> {
        val pageRequest: PageRequest = PageRequest.of(page, size)
        return groupRepository.findByParentId(groupID, pageRequest).content.map {
            GroupRepresentation(
                id = it.id!!,
                name = it.name,
                parentId = it.parent?.id,
                enable = it.enable!!
            )
        }
    }

    override fun joinUserToGroup(user: User, groupID: String) {
        val groupOptional = this.groupRepository.findById(groupID)
        if(groupOptional.isPresent){
            val group = groupOptional.get()
            group.users = group.users.plus(user)
            this.groupRepository.save(group)
        }else{
            throw NotFoundException("Group not found")
        }
    }

    override fun getGroupMembers(groupID: String, size: Int, page: Int): List<UserRepresentation> {
        val groupOptional = groupRepository.findById(groupID)
        return if(groupOptional.isPresent){
            groupOptional.get().users.map {
                UserRepresentation(
                    id = it.userID,
                    name = if(it.name != null) it.name!! else "",
                    email = it.email,
                    enable = if(it.enabled != null) it.enabled!! else false
                )
            }
        }else{
            listOf()
        }
    }

    override fun removeMemberFromGroup(groupID: String) {
        val subGroups = groupRepository.findByParentId(groupID)
        subGroups.stream().forEach{
            val subGroup = groupRepository.findById(it.id!!).get()
            subGroup.parent = null
            groupRepository.save(subGroup)
        }

        groupRepository.findById(groupID).ifPresent {
            groupRepository.deleteById(it.id!!)
        }
    }

    override fun getGroupByUser(size: Int, page: Int): Stream<Group> {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        if(userId.isBlank()) throw NotFoundException("User not found")
        return groupRepository.findByOwnerUserID(userId, PageRequest.of(page, size)).stream()
    }

    override fun getCountGroupByUser(isTopGroup: Boolean): Int {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        if(userId.isBlank()) throw NotFoundException("UserID must null, blank")
        return if(isTopGroup){
            if(userId.isBlank()) throw NotFoundException("User not found")
            groupRepository.countByParentIsNullAndOwnerUserID( userId)
        }else{
            groupRepository.countByOwnerUserID(userId)
        }
    }

    override fun getCountGroupByUser(name: String, extract: Boolean): Int {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        return if(extract){
            groupRepository.countByNameEqualsAndOwnerUserID(name, userId)
        } else groupRepository.countByNameContainingIgnoreCaseAndOwnerUserID(name, userId)
    }

}


