package autogear.frontapi.listener

import autogear.frontapi.entity.AbstractEntity
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent

import org.springframework.stereotype.Component


@Component
class CollectionsListener{

    private val logger = LoggerFactory.getLogger(
        CollectionsListener::class.java
    )

    @EventListener
    fun handleAfterSave(event: AfterSaveEvent<*>){
        if(event.source is AbstractEntity){
            val entity = event.source as AbstractEntity
            logger.info("AfterSaveEvent: ${entity.id}")
        }
    }

//    @EventListener
//    fun handleBeforeSave(event: BeforeSaveEvent<*>) {
//        if (event.source is AbstractEntity) {
//            val entity = event.source as AbstractEntity
//            logger.info("BeforeSaveEvent: ${entity.id}")
//            val securityContext = SecurityContextHolder.getContext()
//            val authentication = securityContext.authentication
//            if (authentication is UserDetails){
//                val user = authentication as AuthenticateService.CustomUserDetails
//                entity.createdBy = user.getUserId()
//                entity.createdDate = LocalDateTime.now()
//                entity.lastModifiedBy = user.getUserId()
//            }
//        }
//    }
}





