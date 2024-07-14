package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "password_reset_token")
data class PasswordResetToken(
    @Id override var id: String? = null,
    var token: String,
    var user: User,
    var expiryDate: Date,
    var isValid: Boolean
) : AbstractEntity()