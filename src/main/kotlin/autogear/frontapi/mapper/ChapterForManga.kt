package autogear.frontapi.mapper

data class ChapterForManga(
    var chapters: List<ChapterItem>? = emptyList()
)

data class ChapterItem(
    val title: String,
    val number: Int
)