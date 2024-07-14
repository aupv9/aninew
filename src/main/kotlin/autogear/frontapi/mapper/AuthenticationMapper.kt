package autogear.frontapi.mapper

import autogear.frontapi.entity.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings


@Mapper(componentModel = "spring")
interface UserMapper {
    @Mappings(
        Mapping(source = "id", target = "id"),
        Mapping(source = "name", target = "name"),
        Mapping(source = "email", target = "email")
    )
    fun toDto(user: User): UserDto
    fun toEntity(userDto: UserDto): User

}

data class UserDto(
    val id: String? = null,
    val name: String? = null,
    val email: String
)
