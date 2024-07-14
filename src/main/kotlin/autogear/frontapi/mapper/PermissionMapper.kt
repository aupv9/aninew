package autogear.frontapi.mapper

import autogear.frontapi.entity.Permission
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring", uses = [PermissionMapper::class]
)
interface PermissionMapper {
    fun toDto(permission: Permission): PermissionDto
    fun toEntity(permissionDto: PermissionDto): Permission
}

