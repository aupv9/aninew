package autogear.frontapi.payload

import org.springframework.web.multipart.MultipartFile
import java.util.*

data class Chapter(
    val number: Int? = 0,
    val title: String? = "",
    val volumeNumber: Int? = 0,
    var pageResource: List<MultipartFile>? = null,
    val pageCount: Int? = 0,
    val uploadDate: Date? = Date()
){
    constructor(): this(0, "", 0, null, 0, Date())
}

