package autogear.frontapi.service.authen

import autogear.frontapi.entity.User
import common.*


interface AuthenticationService{
    fun login(username: String, password: String): ResponseAuthenticate
    fun logout(accessToken: String, refreshToken: String): Boolean
    fun isLogin(token: String): Boolean
    fun signUp(signUpRequest:RegisterRequest): User
    fun isExited(email: String): Boolean
    fun createSession(userID: String, email: String): ResponseAuthenticate
    fun newTokenFromRefresh(refreshToken: String): ResponseAuthenticate
    fun changePassword(request: PasswordRequest): Boolean
    fun resetPassword(email: String): Boolean

    fun havePermission(userID: String, permission: String): Boolean
}