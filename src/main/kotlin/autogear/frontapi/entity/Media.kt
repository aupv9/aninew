package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "media")
data class Media(
    @Id
    override var id: String? = null,
    var url: String? = null,
    var type: String? = null,
    var size: Long? = null,
    var tilte: String? = null,
    var description: String? = null,
    var tags: List<String>? = null,
    var metadata: Map<String, String>? = null,

): AbstractEntity()