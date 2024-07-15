package autogear.frontapi.mapper

import autogear.frontapi.entity.Metadata

data class PublicationDTO(
    val id: String,
    val type: String,
    val author: String,
    val artist: String?,
    val publisher: String,
    val status: String,
    val releaseYear: Int,
    val genres: List<String>,
    val description: String,
    var coverImageURL: String? = null,
    val metadata: Metadata? = null
)
