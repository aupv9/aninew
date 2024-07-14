package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp
import java.util.*

const val EXPIRATION = 1 * 15
@Document(collection = "verificationToken")
data class VerificationToken(
    @Id override var id: String? = null,
    var token: String,
    var user: User,
    var expiryDate: Date = calculateExpiryDate(EXPIRATION),
    var isValid: Boolean = true
) : AbstractEntity(id){
    companion object {
        fun calculateExpiryDate(expiryTimeInMinutes: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.time = Timestamp(calendar.time.time)
            calendar.add(Calendar.MINUTE, expiryTimeInMinutes)
            return calendar.time
        }
    }

}

