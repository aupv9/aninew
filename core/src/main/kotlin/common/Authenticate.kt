package common


data class LoginRequest(
    val email: String,
    val password: String
)


data class RegisterRequest(
    val email: String,
    val password: String,
    val timeZone: String = "Asia/Jakarta"
)

data class VerifyTokenResend(
    val email: String
)

data class ResponseAuthenticate(
    val accessToken: String,
    val refreshToken: String,
    val isLogin: Boolean
)

data class PasswordRequest(
    val password: String,
    val oldPassword: String
)

data class UserPermission(
    val permission: String
)

data class WhoAmI(
    var isManageApp: Boolean? = null,
    var displayName: String? = "",
    var locale: String? = "",
    var userID: String? = "",
    var app:String? = "",
    var appAccess: Map<String, Set<String>>? = emptyMap()
)

data class CommonResponseList<T>(
    var data: List<T>? = null
)

data class CommonRequest<T>(
    var data: T? = null
)
data class CommonResponse<T>(var data: T? = null, var status: Boolean? = null, var message: String? = null, var code: Int? = null)