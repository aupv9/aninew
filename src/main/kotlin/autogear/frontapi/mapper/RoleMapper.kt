package autogear.frontapi.mapper

import autogear.frontapi.entity.Role
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings


@Mapper(componentModel = "spring")
interface RoleMapper {

    @Mappings(
        Mapping(source = "id", target = "id"),
        Mapping(source = "name", target = "name"),
        Mapping(source = "description", target = "description"),
        Mapping(source = "permission", target = "permissions"),
    )
    fun toDto(role: Role): RoleDto
    fun toEntity(roleDto: RoleDto): Role
}