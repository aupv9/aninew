package autogear.frontapi.mapper

import com.fasterxml.jackson.annotation.JsonIgnore


data class FileDto(
    var filename: String? = "",
    var contentType: String,
    var size: Long,
    @JsonIgnore
    var content: String ? = null,
    @JsonIgnore
    var providerId: String? = null,
 )
