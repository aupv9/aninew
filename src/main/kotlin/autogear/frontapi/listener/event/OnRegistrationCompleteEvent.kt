package autogear.frontapi.listener.event

import autogear.frontapi.entity.User
import org.springframework.context.ApplicationEvent
import java.util.*

class OnRegistrationCompleteEvent(
    val appUrl: String,
    val locale: Locale,
    val user: User
) : ApplicationEvent(user)