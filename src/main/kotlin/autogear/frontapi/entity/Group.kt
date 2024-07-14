package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(
    collection = "groups"
)
class Group(
    @Id
    override var id: String? = null,
    var name: String,
    var enable: Boolean? = true,
    var users: Set<User> = emptySet(),
    var owner: User,
    var parent:  Group? = null
): AbstractEntity(){
    fun removeChild(subGroup: Group) {
        if(subGroup.id == this.id) return
        subGroup.parent = null
    }

    fun addChild(subGroup: Group) {
        if(subGroup.id == this.id) return
        subGroup.parent = this
    }
}

