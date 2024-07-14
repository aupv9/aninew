package autogear.frontapi.listener.event

import autogear.frontapi.entity.User
import org.springframework.context.ApplicationEvent

class OnResetPasswordEvent(
    val appUrl: String,
    val user: User
): ApplicationEvent(user)
