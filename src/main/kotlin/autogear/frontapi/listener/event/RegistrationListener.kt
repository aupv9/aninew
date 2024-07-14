package autogear.frontapi.listener.event

import autogear.frontapi.service.authen.IVerifyTokenService
import common.Utils
import org.springframework.context.ApplicationListener
import org.springframework.context.MessageSource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer


@Component
class RegistrationListener(
//    @Qualifier("sendgridService") private val sendGridMailService: SendGridMailService,
//    private val zohoMailService: ZohoMailService,
    private val messageSource: MessageSource,
    private val verifyTokenService: IVerifyTokenService,
//    private val gmailMailService: GmailMailService
    private val javaMailSender: JavaMailSender,
    private val freeMarkerConfigurer: FreeMarkerConfigurer,
): ApplicationListener<OnRegistrationCompleteEvent> {


    @Transactional(rollbackFor = [
        RuntimeException::class
    ])
    @Async
    override fun onApplicationEvent(event: OnRegistrationCompleteEvent) {
        this.confirmRegistration(event)
    }

    fun confirmRegistration(event: OnRegistrationCompleteEvent){
        val user = event.user
        val token = Utils.generateTokenWithUUID()

        val verifyToken = this.verifyTokenService.getVerificationTokenByUser(user)

        verifyToken.ifPresentOrElse({
           it.token = token
            this.verifyTokenService.updateVerificationToken(it)
        },{
            this.verifyTokenService.createVerificationTokenForUser(user, token)
        })

        val recipientAddress = user.email
        val subject = "Registration Confirmation"
        val confirmationUrl = event.appUrl + "?token=" + token
        val freeMarkerTemplate = freeMarkerConfigurer.configuration.getTemplate("registerToEmail.ftl")
        val htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, mapOf(
            "confirmationUrl" to confirmationUrl
        ))

//        val message = javaMailSender.createMimeMessage()
//        val helper = MimeMessageHelper(message, true, "UTF-8")
//        helper.setFrom("sendmailticket@gmail.com")
//        helper.setTo(recipientAddress)
//        helper.setSubject(subject)
//        helper.setText(htmlBody, true)
//        try {
//            javaMailSender.send(message)
//        }catch (_: Exception){
//
//        }
    }


}

