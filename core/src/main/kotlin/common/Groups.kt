package common

import jakarta.validation.constraints.NotBlank


data class NewGroupsRequest(
    @NotBlank
    val name: String,
    val enable: Boolean,
    val parentId: String,
)

data class GroupRepresentation(
    val id: String,
    @NotBlank(message = Message.GROUP_NAME_MISSING)
    val name: String,
    val enable: Boolean = true,
    val parentId: String? = null,
    val subGroupCount: Int = 0,
    val subGroups: List<GroupRepresentation>? = emptyList()
)

interface Message{
    companion object{
        const val GROUP_NAME_MISSING: String = "Group name is missing"
        const val GROUP_NOT_FOUND: String = "Group not found"
    }
}
