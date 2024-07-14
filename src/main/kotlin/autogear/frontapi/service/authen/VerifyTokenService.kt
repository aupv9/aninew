package autogear.frontapi.service.authen

import autogear.frontapi.entity.User
import autogear.frontapi.entity.VerificationToken
import autogear.frontapi.repository.RoleRepository
import autogear.frontapi.repository.UserRepository
import autogear.frontapi.repository.VerifyTokenRepo
import common.Utils
import org.springframework.stereotype.Service
import java.util.*


@Service
class VerifyTokenService(
    private val verifyTokenRepo: VerifyTokenRepo,
    private val authenticateRepository: UserRepository,
    private val roleRepository: RoleRepository
): IVerifyTokenService {
    override fun verifyToken(token: String): Boolean {
        val verificationTokenOption = verifyTokenRepo.findByToken(token)
        return if (!verificationTokenOption.isPresent) false
        else {
            with(verificationTokenOption){
                val verificationToken = this.get()
                val user = verificationToken.user
                val calendar = Calendar.getInstance()
                if(!verificationToken.isValid) return false
                val isExpired = run {
                    val isExpired = Utils.isTokenExpired(verificationToken.expiryDate, calendar.time)
                    if(!isExpired) verifyTokenRepo.delete(verificationToken)
                    isExpired
                }
                if(isExpired) {
                    val role = roleRepository.findByName(autogear.frontapi.enum.Role.ROLE_ADMIN.name)
                    user.enabled = true
                    user.roles = listOf(role!!)
                    authenticateRepository.save(user)
                    verificationToken.isValid = false
                    verifyTokenRepo.save(verificationToken)
                }
                isExpired
            }
        }
    }

    override fun createVerificationTokenForUser(user: User, token: String) {
        val verificationToken = VerificationToken(token = token, user = user)
        verifyTokenRepo.save(verificationToken)
    }

    override fun getVerificationToken(token: String): Optional<VerificationToken> = this.verifyTokenRepo.findByToken(token)

    override fun getVerificationTokenByUser(user: User): Optional<VerificationToken> {
        return verifyTokenRepo.findByUserUserID(user.userID)
    }

    override fun updateVerificationToken(verificationToken: VerificationToken) {
        this.verifyTokenRepo.save(verificationToken)
    }

}