package autogear.frontapi.service

import autogear.frontapi.entity.Group
import autogear.frontapi.entity.User
import common.GroupRepresentation
import common.UserRepresentation
import java.util.stream.Stream

interface IGroupService {
    fun createNewGroup(groupRep: GroupRepresentation): Boolean

    fun getGroupById(id: String): Group?

    fun moveGroup(group: Group, toParent: Group?)

    fun getChildrenByGroup(groupID: String, size: Int, page: Int): List<GroupRepresentation>

    fun joinUserToGroup(user: User, groupID: String)

    fun getGroupMembers(groupID: String, size: Int, page: Int): List<UserRepresentation>

    fun removeMemberFromGroup(groupID: String)

    fun getGroupByUser(size: Int, page: Int): Stream<Group>

    fun getCountGroupByUser(isTopGroup: Boolean): Int

    fun getCountGroupByUser(name: String, extract: Boolean): Int

}