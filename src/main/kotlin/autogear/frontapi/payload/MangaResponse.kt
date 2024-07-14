package autogear.frontapi.payload

import autogear.frontapi.entity.ResourceMetadata

data class MangaResponse(
    var id: String? = null,
    var metadata: ResourceMetadata? = null,
    var chapters: List<ChapterResponse>? = emptyList(),
)

data class ChapterResponse(
    var title: String,
    var number: Int,
    var pages: List<PageResponse>? = emptyList()
)

data class PageResponse(
    var number: Int,
    var resourceUrl: String? = null
)