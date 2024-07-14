package autogear.frontapi.service.authen

import autogear.frontapi.entity.User
import autogear.frontapi.entity.VerificationToken
import java.util.Optional

interface IVerifyTokenService{
    fun verifyToken(token: String): Boolean
    fun createVerificationTokenForUser(user: User, token: String)
    fun getVerificationToken(token: String): Optional<VerificationToken>
    fun getVerificationTokenByUser(user: User): Optional<VerificationToken>
    fun updateVerificationToken(verificationToken: VerificationToken)

}