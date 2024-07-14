package autogear.frontapi.mapper

data class RoleDto(
    val id: String? = null,
    val name: String? = null,
    val description: String,
    var permissions: List<PermissionDto?>
)