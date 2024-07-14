package autogear.frontapi.mapper

import autogear.frontapi.entity.ResourceMetadata

data class MangaDto(
    var id: String? = null,
    var metadata: ResourceMetadata? = null,
    var providerId: String? = "",
    var chapters: List<ChapterDto>? = emptyList(),
)

