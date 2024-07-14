package autogear.frontapi.exception

data class VerificationInvalidException (override val message: String?): Exception(message)