package autogear.frontapi.exception.handle

import autogear.frontapi.exception.NotFoundException
import autogear.frontapi.exception.UserInactiveException
import autogear.frontapi.exception.VerificationInvalidException
import common.CommonResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception

@RestControllerAdvice
class ExceptionHandling {

    @ExceptionHandler(value = [UserInactiveException::class, VerificationInvalidException::class])
    fun handleUserInactiveException(exception: Exception): ResponseEntity<*>{
        val commonResponse = CommonResponse(data = null)
        when(exception){
            is UserInactiveException, is VerificationInvalidException -> {
                commonResponse.message = exception.message
                commonResponse.code = HttpStatus.BAD_REQUEST.value()
                commonResponse.status = false
            }
        }
        return ResponseEntity.badRequest().body(commonResponse)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleException(exception: NotFoundException): ResponseEntity<*>{
        val commonResponse = CommonResponse(data = null)
        commonResponse.message = exception.message
        commonResponse.code = exception.status.value()
        commonResponse.status = false
        return ResponseEntity.badRequest().body(commonResponse)
    }

}