package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "executions")
data class Execution(
    @Id override var id: String? = null,
    val jobId: String,
    val executionTime: Date = Date(),
    var status: String,
    val logs: List<LogEntry> = emptyList(),
    val error: String? = null
): AbstractEntity() {
    data class LogEntry(val logTime: Date = Date(), val message: String)
}