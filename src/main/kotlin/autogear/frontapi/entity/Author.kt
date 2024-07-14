package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "authors")
data class Author(
    @Id
    override var id: String?,
    var name: String,
): AbstractEntity()
