package autogear.frontapi.mapper

import autogear.frontapi.entity.Manga
import org.mapstruct.Mapper
import org.mapstruct.Mapping


@Mapper(
    componentModel = "spring",
    uses = [MangaMapper::class]
)
interface MangaMapper: IMapper<MangaDto, Manga> {
    override fun toEntity(resource: MangaDto): Manga
    override fun toDto(entity: Manga): MangaDto
}