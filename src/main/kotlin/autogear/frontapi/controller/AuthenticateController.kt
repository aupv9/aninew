package autogear.frontapi.controller

import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.entity.User
import autogear.frontapi.exception.UserInactiveException
import autogear.frontapi.exception.VerificationInvalidException
import autogear.frontapi.listener.event.OnRegistrationCompleteEvent
import autogear.frontapi.service.IUserService
import autogear.frontapi.service.authen.AuthenticationService
import autogear.frontapi.service.authen.IVerifyTokenService
import common.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/v1/api/auth")
@Tag(name = "Authentication")
class AuthenticateController(
    val authenticationService: AuthenticationService,
    val userService: IUserService,
    val eventPublisher: ApplicationEventPublisher,
    val verifyTokenService: IVerifyTokenService,
    val autoGearConfiguration: AutoGearConfiguration,
    val messageSource: MessageSource
){

    private fun getMessage(key: String, default: String? = null): String? = messageSource.getMessage("authenticate.user.$key", null, default, Locale.ENGLISH )



    @Operation(
        summary = "User Login"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login success", content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]),
            ApiResponse(responseCode = "400", description = "Login fail Or Account not active now", content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]),
            ApiResponse(responseCode = "403", description = "UserName or Password not correct", content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)])
        ]
    )
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest, httpServletResponse: HttpServletResponse): ResponseEntity<CommonResponse<Boolean>> {
        val user = this.userService.findUserByEmail(loginRequest.email)
        user.ifPresentOrElse({
            if(it.enabled == false) throw UserInactiveException(getMessage("inactive", "Account not active now"))
        },{
            throw UserInactiveException(getMessage("not-register", "UserName or Password not correct"))
        })

        val responseLogin = authenticationService.login(loginRequest.email, loginRequest.password)
        this.generateCookie(responseLogin, httpServletResponse)

        val loginSuccess = responseLogin.accessToken.isNotEmpty()
        return ResponseEntity.ok(CommonResponse(status = loginSuccess, code = HttpStatus.OK.value()))
    }

    private fun generateCookie(response: ResponseAuthenticate, httpServletResponse: HttpServletResponse){
        val sessionCookie = Cookie(Utils.USER_SESSION, response.accessToken)
//      sessionCookie.isHttpOnly = true
//      sessionCookie.secure = true
        sessionCookie.maxAge = 15 * 60
        sessionCookie.path = "/"
//      sessionCookie.domain = ".localhost"

        httpServletResponse.addCookie(sessionCookie)

        val refreshCookie = Cookie(Utils.AG_SESSION, response.refreshToken)
//      refreshCookie.isHttpOnly = true
//      refreshCookie.secure = true
        refreshCookie.maxAge = 15 * 60 * 24
        refreshCookie.path = "/"
//      refreshCookie.domain = ".localhost"

        httpServletResponse.addCookie(refreshCookie)

        val isLoginCookie = Cookie(Utils.LOGGED_IN, response.isLogin.toString())
//      isLoginCookie.isHttpOnly = true
//      isLoginCookie.secure = true
        isLoginCookie.maxAge = 15 * 60
        isLoginCookie.path = "/"
//      isLoginCookie.domain = ".localhost"

        httpServletResponse.addCookie(isLoginCookie)
    }

    @Operation(
        summary = "Confirm account"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Confirm Success", content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]),
            ApiResponse(responseCode = "400", description = "Verify Invalid", content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)])
        ]
    )
    @GetMapping("/signup/confirm", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun confirmRegistration(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse,
                            @RequestParam("token") verifyToken: String): ResponseEntity<CommonResponse<Boolean>> {
        val isValidToken = verifyTokenService.verifyToken(verifyToken)
        if (isValidToken){
            with(verifyTokenService.getVerificationToken(token = verifyToken)){
                if(this.isPresent){
                    val user = this.get().user
                    val responseLogin = authenticationService.createSession(user.userID, user.email)
                    generateCookie(responseLogin, httpServletResponse)
                    val cookieFirstLogged = Cookie(Utils.FIRST_LOGGED, true.toString())
                    cookieFirstLogged.maxAge = 15 * 60
                    httpServletResponse.addCookie(cookieFirstLogged)
                }
            }
        }else{
            throw VerificationInvalidException(getMessage("invalid-verify-token", "Verify Invalid"))
        }
        return ResponseEntity.ok(CommonResponse(status = true, code = HttpStatus.OK.value(), message = getMessage("success", "Confirm Success")))
    }


    @Operation(
        summary = "Resend Verify"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Confirm Success", content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]),
            ApiResponse(responseCode = "400", description = "Verify Invalid", content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)])
        ]
    )
    @PostMapping("/signup/resendVerify", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun resendVerifyToken(@RequestBody request: VerifyTokenResend, httpServletRequest: HttpServletRequest): ResponseEntity<CommonResponse<Boolean>> {
        val user = userService.findUserByEmail(request.email)
        if(!user.isPresent) return ResponseEntity.badRequest().body(CommonResponse(status = false, message = getMessage(key = "not-find-account", "Link confirm already send to email")))
        val message = getMessage(key = "resend-token", "Link confirm already send to email")
        return this.sendEvent(httpServletRequest, user.get(), message!! )
    }


    @PostMapping("/signup/new", produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun signup(@RequestBody  request: RegisterRequest, httpServletRequest: HttpServletRequest): ResponseEntity<CommonResponse<Boolean>>{
        val isExited = authenticationService.isExited(request.email)
        if(isExited) return ResponseEntity.badRequest().body(CommonResponse(message = getMessage(key = "already-email", "Email exited"), status = false, code = HttpStatus.BAD_REQUEST.value()))
        val user = authenticationService.signUp(request)
        val message = getMessage(key = "register-success", "Register Success")
        return this.sendEvent(httpServletRequest, user, message!!)
    }


    @PostMapping("/refresh", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun receiveNewAccessToken(@CookieValue(Utils.AG_SESSION, required = false) refreshToken: String,
                              httpServletResponse: HttpServletResponse): ResponseEntity<CommonResponse<Nothing>> {
        val response = authenticationService.newTokenFromRefresh(refreshToken)
        this.generateCookie(response, httpServletResponse)
        val responseTotal: CommonResponse<Nothing> = if (response.accessToken.isNotEmpty()) CommonResponse(null, true, getMessage("", "Renew Token Success"), 200)
                                                     else CommonResponse(null, false, getMessage("", "Renew Token Fail"), 400)
        return ResponseEntity.ok(responseTotal)
    }


    @DeleteMapping("/logout")
    fun logout(httpServletResponse: HttpServletResponse, @CookieValue(Utils.USER_SESSION, required = false) accessToken: String,
               @CookieValue(Utils.AG_SESSION, required = false) rfToken: String): ResponseEntity<*>{
        val result = authenticationService.logout(accessToken, rfToken)
        val cookieLogged = Cookie(Utils.LOGGED_IN, false.toString())
        cookieLogged.path = "/"
        httpServletResponse.addCookie(cookieLogged)

        val cookieUSS = Cookie(Utils.USER_SESSION, null)
        cookieUSS.maxAge = 0
        cookieUSS.path = "/"
        httpServletResponse.addCookie(cookieUSS)

        val cookieRFT = Cookie(Utils.AG_SESSION, null)
        cookieRFT.maxAge = 0
        cookieRFT.path = "/"
        httpServletResponse.addCookie(cookieRFT)

        return ResponseEntity.ok(CommonResponse(null, result, "Logout Success", 200))
    }

    private fun sendEvent(httpServletRequest: HttpServletRequest, user: User, message: String): ResponseEntity<CommonResponse<Boolean>>{
        val appURLClient = with(autoGearConfiguration.clientConfiguration.urlAppClient){ this.ifBlank { getAppUrl(httpServletRequest) } }
        eventPublisher.publishEvent(OnRegistrationCompleteEvent(httpServletRequest.scheme + "://$appURLClient", httpServletRequest.locale, user ))
        return ResponseEntity.ok(CommonResponse(status = true, code = HttpStatus.OK.value(), message = message))
    }


    private fun getClientIP(request: HttpServletRequest): String{
        return request.getHeader("X-Forwarded-For") ?: request.remoteAddr
    }

    private fun getAppUrl(httpServletRequest: HttpServletRequest): String = httpServletRequest.scheme.plus("://").plus(httpServletRequest.serverName).plus(":")
        .plus(httpServletRequest.serverPort).plus("/").plus(autoGearConfiguration.api.version).plus("/api/auth").plus(httpServletRequest.contextPath)

}