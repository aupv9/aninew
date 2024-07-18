package autogear.frontapi.payload

import java.util.*

data class ChapterDetail(
    val number: Int,
    val title: String,
    val volumeNumber: Int,
    var pageDetail: Collection<PageContentDetail>?=  emptyList(),
    var pageCount: Int? = 0,
    val uploadDate: Date
)

data class PageContentDetail(
    var number: Int? = 0,
    var presignUrl: String? = ""
)