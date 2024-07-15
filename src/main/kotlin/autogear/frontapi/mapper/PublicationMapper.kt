package autogear.frontapi.mapper

import autogear.frontapi.entity.Publication
import org.mapstruct.Mapper

@Mapper(componentModel = "Spring", uses = [PublicationMapper::class])
interface PublicationMapper: IMapper<PublicationDTO, Publication> {
}