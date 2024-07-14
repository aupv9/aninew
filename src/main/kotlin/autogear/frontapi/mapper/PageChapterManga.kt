package autogear.frontapi.mapper


data class PageChapterManga(
    val page: Int,
    var description: String? = null
) {
    constructor() : this(0)
}

