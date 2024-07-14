package common

data class RoleRepresentation(
    val id: String,
    val name: String,
    val description: String?,
    val attributes: Map<String, List<String>>? = emptyMap(),
    val permission: List<Permission>? = emptyList()
)

data class Permission(
    val name: String
)