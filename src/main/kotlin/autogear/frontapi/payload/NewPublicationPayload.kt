package autogear.frontapi.payload

import autogear.frontapi.entity.PublicationType


data class NewPublicationPayload(
    val title: String,
    val type: PublicationType, // "manga", "book", or "comic"
    val author: String,
    val artist: String?,
    val publisher: String,
    val status: String,
    val releaseYear: Int,
    val genres: List<String>,
    val description: String
)
