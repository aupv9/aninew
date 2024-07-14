package autogear.frontapi.mapper

import autogear.frontapi.entity.Chapter
import org.mapstruct.Mapper


@Mapper(
    componentModel = "spring",
    uses = [ChapterMapper::class]
)
interface ChapterMapper: IMapper<ChapterDto, Chapter> {
    override fun toEntity(resource: ChapterDto): Chapter

    override fun toDto(entity: Chapter): ChapterDto
}