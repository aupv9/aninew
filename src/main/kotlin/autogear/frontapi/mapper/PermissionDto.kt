package autogear.frontapi.mapper

data class PermissionDto(
    var id: String? = null,
    val name: String,
    val description: String? =  null,
)