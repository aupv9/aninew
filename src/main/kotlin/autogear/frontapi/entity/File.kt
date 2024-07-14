package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "file")
data class File(
    @Id
    override var id: String? = null,
    var fileName: String? = null,
    var fileType: String? = null,
    var fileSize: Long? = null,
    var filePath: String? = null,
    var folderID: String? = null
): AbstractEntity()