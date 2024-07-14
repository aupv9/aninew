package autogear.frontapi.configuration

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer


interface IMailService{
    fun mailServiceConfiguration(): JavaMailSender

    fun preMailServiceConfiguration(): JavaMailSenderImpl {
        return JavaMailSenderImpl()
    }
}

interface ThemeTemplateEmail{
    fun sendMessageUsingFreemarkerTemplate(to: String, subject: String, templateModel: Map<String, Any>)
}




class MailServiceContext(private val mailService: IMailService) {
    fun makeMailService() = mailService.mailServiceConfiguration()
}


@Component("gmailService")
class GmailMailService: IMailService {

    @Autowired
    private lateinit var freeMarkerConfigurer: FreeMarkerConfigurer

    override fun mailServiceConfiguration(): JavaMailSender {
        val mailSender = this.preMailServiceConfiguration()
        mailSender.host = "smtp.gmail.com"
        mailSender.username = "sendmailticket@gmail.com"
        mailSender.password = "supersadio@2021"
        val props = mailSender.javaMailProperties
        props["smtp.auth"] = true
        props["smtp.starttls.enable"] = true
        return mailSender
    }



}

@Component("sendgridService")
@ConditionalOnBean(SendGrid::class)
class SendGridMailService(private val sendgrid: SendGrid): IMailService{
    override fun mailServiceConfiguration(): JavaMailSender {
        TODO("Not yet implemented")
    }

    fun sendSingleMail(mail: Mail){
        val request = Request()
        request.method = Method.POST
        request.endpoint = "mail/send"
        request.body = mail.build()

        val response = sendgrid.api(request)
        if(response.statusCode < 200 || response.statusCode >= 300) throw RuntimeException(response.body)
    }
}

@Component("zohoService")
class ZohoMailService: IMailService{
    override fun mailServiceConfiguration(): JavaMailSender {
        val mailSender = this.preMailServiceConfiguration()
        mailSender.host = "smtp.zoho.com"
        mailSender.port = 587
        mailSender.username = "aupv96@zohomail.com"
        mailSender.password = "Supersadio@2023"
        val props =  mailSender.javaMailProperties
        props["mail.smtp.starttls.enable"] = true
        props["mail.smtp.ssl.trust"] = "smtp.zoho.com"
        props["mail.smtp.port"] = 465
        props["mail.smtp.socketFactory.port"] = 465
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.debug"] = true
        return mailSender
    }

}