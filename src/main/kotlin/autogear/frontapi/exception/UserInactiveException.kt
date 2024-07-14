package autogear.frontapi.exception


data class UserInactiveException(override val message: String?) : Exception(message)