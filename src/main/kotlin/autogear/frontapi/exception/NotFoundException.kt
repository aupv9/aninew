package autogear.frontapi.exception

import org.springframework.http.HttpStatus

data class NotFoundException(
    override val message: String? = "Not Found",
    override val cause: Throwable? = null,
    val status: HttpStatus
) : RuntimeException(message, cause) {
    constructor(message: String, cause: Throwable) : this(message, cause, HttpStatus.NOT_FOUND)
    constructor(message: String) : this(message, null, HttpStatus.NOT_FOUND)
    constructor(cause: Throwable) : this(cause.message ?: "Not Found", cause, HttpStatus.NOT_FOUND)
    constructor() : this(null, null, HttpStatus.NOT_FOUND)
}
