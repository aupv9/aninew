package autogear.frontapi.enum

interface BasePermission {
    fun getName(): String
}

interface BaseRole {
    fun getPermissions(): Set<BasePermission>
}

enum class AdminPermission: BasePermission {
    CREATE_GROUP{ override fun getName(): String = "create-group" },
    DELETE_GROUP{ override fun getName(): String = "delete-group" },
    ADD_MEMBER_TO_GROUP{ override fun getName(): String = "add-member-to-group" },
    CREATE_NEW_ROLE{ override fun getName(): String = "create-new-role" },
    UPDATE_ROLE{ override fun getName(): String = "update-role" },
    MANAGER_USER{ override fun getName(): String = "manager-user" },
    MANAGER_ROLE{ override fun getName(): String = "manager-role" },
    MANAGE_APP{ override fun getName(): String = "manage-app" }
    ;
    companion object {

         val ALL_REALM_ROLES = setOf(
             MANAGER_USER,
             MANAGER_ROLE,
             CREATE_NEW_ROLE
        )
    }
    fun parseFromName(name: String): AdminPermission {
        if ("create-group" == name) return CREATE_GROUP
        if ("delete-group" == name) return DELETE_GROUP
        if ("add-member-to-group" == name) return ADD_MEMBER_TO_GROUP
        throw Exception("Permission name is not valid")
    }
}



enum class UserMemberPermission: BasePermission {
    CREATE_GROUP_FOR_MEMBER{ override fun getName(): String = "create-group-for-user" },
    DELETE_GROUP_FOR_MEMBER{ override fun getName(): String = "delete-group-for-user" },
    ADD_MEMBER_TO_GROUP_FOR_MEMBER{ override fun getName(): String = "add-member-to-group" },
    REMOVE_MEMBER_FROM_GROUP_FOR_MEMBER{ override fun getName(): String = "remove-member" },
    ADD_CHILD_GROUP_FOR_MEMBER{ override fun getName(): String = "add-child-group" },
    GET_ALL_CHILD_GROUP_FOR_MEMBER{ override fun getName(): String = "get-all-child-group" },
    MANAGE_GROUP_FOR_MEMBER{ override fun getName(): String = "manage-group" },
    MANAGE_PROFILE_USER{ override fun getName(): String = "manage-profile" }
    ;

    fun parseFromName(name: String): UserMemberPermission{
        if("create-group-for-user" == name) return CREATE_GROUP_FOR_MEMBER
        if("delete-group-for-user" == name) return DELETE_GROUP_FOR_MEMBER
        if("add-member-to-group" == name) return ADD_MEMBER_TO_GROUP_FOR_MEMBER
        if("remove-member" == name) return REMOVE_MEMBER_FROM_GROUP_FOR_MEMBER
        throw Exception("Permission name is not valid")
    }
}


enum class Role: BaseRole{
    ROLE_USER {
        override fun getPermissions(): Set<BasePermission> =  UserMemberPermission.values().toSet()
    },
    ROLE_ADMIN {
        override fun getPermissions(): Set<BasePermission> = AdminPermission.values().toSet()
    }
}

enum class ResourcePermission: BasePermission{
    CREATE_RESOURCE { override fun getName(): String = "create-resource" },
    GET_ALL_RESOURCE { override fun getName(): String = "get-all-resource" },
    UPDATE_RESOURCE { override fun getName(): String = "update-resource" }
}