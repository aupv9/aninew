package autogear.frontapi.entity

import org.springframework.data.annotation.*
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime



open class AbstractEntity(
    open var id: String? = null,
    @Field("created_by")
    @CreatedBy var createdBy: String? = null,
    @Field("created_date")
    @CreatedDate var createdDate: LocalDateTime? = null,
    @Field("last_modified_by")
    @LastModifiedBy var lastModifiedBy: String? = null,
    @Field("last_modified_date")
    @LastModifiedDate var lastModifiedDate: LocalDateTime? = null
)














