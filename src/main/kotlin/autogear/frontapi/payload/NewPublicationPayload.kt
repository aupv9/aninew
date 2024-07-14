package autogear.frontapi.payload

import autogear.frontapi.entity.PublicationType



data class NewPublicationPayload(
   val title: String? = "",
   val type: PublicationType? = PublicationType.MANGA, // "manga", "book", or "comic"
   val author: String? = "",
   val artist: String? = "",
   val publisher: String? = "",
   val status: String? = "",
   val releaseYear: Int,
   val genres: List<String>? = listOf(),
   val description: String? = ""
){
   constructor():this("", PublicationType.MANGA, "", "", "", "", 0, listOf(), "")
}
