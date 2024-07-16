package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document(collection = "publications")
data class Publication(
    @Id
    override var id: String? = null,
    @Field("title")
    val title: String,
    @Field("publication_type")
    val type: PublicationType, // "manga", "book", or "comic"
    @Field("author")
    val author: String,
    @Field("artist")
    val artist: String?,
    @Field("publisher")
    val publisher: String,
    @Field("status")
    val status: String,
    @Field("release_year")
    val releaseYear: Int,
    @Field("genres")
    val genres: List<String>,
    @Field("description")
    val description: String,
    @Field("cover_image_key")
    var coverImageKey: String? = null,
    @Field("volumes")
    val volumes: List<Volume>? = emptyList(),
    @Field("chapters")
    val chapters: List<Chapter>? = emptyList(),
    @Field("metadata")
    val metadata: Metadata ? = null
): AbstractEntity()

enum class PublicationType {
    MANGA, BOOK, COMIC
}

data class Volume(
    val number: Int,
    val title: String,
    val coverImageKey: String,
    val releaseDate: Date
)

data class ChapterPub(
    val number: Int,
    val title: String,
    val volumeNumber: Int,
    var pageKeys: List<String>?=  emptyList(),
    var pageCount: Int? = 0,
    val uploadDate: Date
)

data class Metadata(
    val averageRating: Double,
    val totalViews: Long,
    val language: String
)