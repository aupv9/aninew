package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "manga")
data class Manga(
    @Id
    override var id: String? = null,
    var metadata: ResourceMetadata,
    var chapters: List<Chapter> = emptyList(),
): AbstractEntity()

data class ResourceMetadata(
    val author: String?,
    val genre: String?,
    var summary: String,
    var datePublished: String?,
    var title: String,
    var language: String,
)

data class Chapter(
    var title: String,
    var number: Int,
    var pageKey: List<String>? = emptyList()
)

data class Page(
    var number: Int
)
