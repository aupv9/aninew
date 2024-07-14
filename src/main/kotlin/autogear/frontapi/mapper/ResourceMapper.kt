package autogear.frontapi.mapper

import autogear.frontapi.entity.Resource
import org.mapstruct.Mapper

@Mapper(componentModel = "spring",  uses = [IMapper::class])
interface ResourceMapper: IMapper<ResourceDTO, Resource> {
    override fun toEntity(resource: ResourceDTO): Resource
    override fun toDto(entity: Resource): ResourceDTO

}