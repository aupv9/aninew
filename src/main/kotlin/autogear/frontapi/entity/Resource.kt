package autogear.frontapi.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.api.services.drive.model.File.ContentHints.Thumbnail
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*


@Document(collection = "resources")
data class Resource(
    @Id
    override var id: String? = null,
    var description: String? = null,
    var provider: ProviderType = ProviderType.LOCAL,
    val title: String? = "",
    val file: FileInfo
): AbstractEntity()

enum class ProviderType {
    CLOUDFLARE, AWS, AZURE, LOCAL, DRIVE
}

enum class ResourceType {
    IMAGE, VIDEO, DOCUMENT, FOLDER
}

data class FileInfo(
    var fileName: String ? = "",
    var contentType: String? = "",
    var size: Long ? = 0,
    var uploadDate: Date? = Date(),
    var path: String? = "",
    var providerId: String? = null,
    @JsonIgnore
    var thumbnail: String? = "",
)

