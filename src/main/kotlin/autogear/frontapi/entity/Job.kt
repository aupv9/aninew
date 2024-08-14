package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "jobs")
data class Job(
    @Id
    override var id: String? = null,
    var userId: String,
    val name: String,
    val platform: Platform,
    val jobType: JobType = JobType.POST_SOCIAL,
    val payload: Payload,
    val schedule: Schedule,
    var status: Status = Status.PENDING,

    ): AbstractEntity(){
    data class Payload(val content: String, val mediaFileId: String?)
    data class Schedule(val startTime: Date, val interval: String, val timezone: String)
}

enum class Platform {
    TWITTER,
    FACEBOOK,
    INSTAGRAM,
    LINKEDIN
}

enum class JobType {
    EMAIL,
    PUSH_NOTIFICATION,
    SMS, POST_SOCIAL
}

enum class Status{
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}