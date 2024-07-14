package autogear.frontapi.controller

import autogear.frontapi.entity.Group
import autogear.frontapi.exception.NotFoundException
import autogear.frontapi.service.IGroupService
import autogear.frontapi.service.UserService
import common.CommonResponse
import common.GroupRepresentation
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/groups")
class GroupsController(
    val groupService: IGroupService,
    val userService: UserService
) {


    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.UserMemberPermission).CREATE_GROUP_FOR_MEMBER)")
    @PostMapping("/", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createNewGroups(@RequestBody @Valid groupRep: GroupRepresentation): ResponseEntity<CommonResponse<Boolean>>{
        val result = groupService.createNewGroup(groupRep)
        return ResponseEntity.ok(CommonResponse(null, result, code = if (result) 200 else 666))
    }


    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.UserMemberPermission).ADD_CHILD_GROUP_FOR_MEMBER) && @groupPermissions.isOwnerGroup(authentication, #groupID)")
    @PostMapping("/{id}/children")
    fun updateGroup(@RequestBody groupRep: GroupRepresentation, @PathVariable("id") groupID: String): ResponseEntity<Any>{
        val groupChild: Group?
        if(groupRep.id.isNotBlank()){
            groupChild = this.groupService.getGroupById(groupRep.id)
            if (groupChild == null) return ResponseEntity.notFound().build()
            if(groupChild.parent?.id != groupID) this.groupService.moveGroup(groupChild, this.groupService.getGroupById(groupID))

            return ResponseEntity.ok(CommonResponse(null, true, code = 200))
        }else{
            this.groupService.createNewGroup(groupRep)
        }
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.UserMemberPermission).GET_ALL_CHILD_GROUP_FOR_MEMBER , T(autogear.frontapi.enum.UserMemberPermission).MANAGE_GROUP_FOR_MEMBER) && @groupPermissions.isOwnerGroup(authentication, #groupID)")
    @GetMapping("/{group-id}/children")
    fun getGroupChildren(@PathVariable("group-id") groupID: String, @RequestParam("first", defaultValue = "0") page: Int,
                         @RequestParam("max", defaultValue = "10") size: Int): ResponseEntity<Any>{
        val childrens = this.groupService.getChildrenByGroup(groupID, size, page)
        return ResponseEntity.ok(CommonResponse(childrens, true, code = 200))
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.UserMemberPermission).ADD_MEMBER_TO_GROUP_FOR_MEMBER , T(autogear.frontapi.enum.UserMemberPermission).MANAGE_GROUP_FOR_MEMBER) && @groupPermissions.isOwnerGroup(authentication, #groupID)")
    @PutMapping("/{group-id}/users/{user-id}")
    fun addUserToGroup(@PathVariable("group-id") groupID: String,
                       @PathVariable("user-id") userId: String): ResponseEntity<Any>{
        val userOptional = this.userService.findUserById(userId)
        if(userOptional.isEmpty) throw NotFoundException("User not found")
        this.groupService.joinUserToGroup(userOptional.get(), groupID)
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.UserMemberPermission).MANAGE_GROUP_FOR_MEMBER) && @groupPermissions.isOwnerGroup(authentication, #groupID)")
    @GetMapping("/{group-id}/members")
    fun getGroupMembers(@PathVariable("group-id") groupID: String,
                        @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("max", defaultValue = "100") size: Int): ResponseEntity<Any> {
        val members = this.groupService.getGroupMembers(groupID, size, page)
        return ResponseEntity.ok(CommonResponse(members, true, code = 200))
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.UserMemberPermission).MANAGE_GROUP_FOR_MEMBER) " +
            "&& @groupPermissions.isOwnerGroup(authentication, #groupID)")
    @DeleteMapping("/{group-id}")
    fun removeGroup(@PathVariable("group-id") groupID: String): ResponseEntity<Any> {
        this.groupService.removeMemberFromGroup(groupID)
        return ResponseEntity.noContent().build()
    }


    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.UserMemberPermission).MANAGE_GROUP_FOR_MEMBER)")
    @GetMapping("/")
    fun getGroupByUser( @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("max", defaultValue = "100") size: Int): ResponseEntity<Any> {
        val members = this.groupService.getGroupByUser(size, page)
        return ResponseEntity.ok(CommonResponse(members, true, code = 200))
    }

    @PreAuthorize("@userPermissionEvaluator.hasPermission(authentication, T(autogear.frontapi.enum.UserMemberPermission).MANAGE_GROUP_FOR_MEMBER)")
    @GetMapping("/count")
    fun countGroup(@RequestParam("search", defaultValue = "") name: String,
                   @RequestParam("top", defaultValue = "false") top: Boolean): ResponseEntity<Any>{
        val map = HashMap<String, Int>()
        val countGroup: Int = if(name.isNotBlank() && !top){
            this.groupService.getCountGroupByUser(name, false)
        }else{
            this.groupService.getCountGroupByUser(top)
        }
        map["count"] = countGroup
        return ResponseEntity.ok(CommonResponse(map, true, code = 200))
    }

}