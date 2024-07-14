package autogear.frontapi.mapper


data class ChapterDto(
    var title: String,
    var number: Int,
    var providerId: String? = null,
    var pages: List<PageDto>? = emptyList()
)