package autogear.frontapi.configuration

import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import java.util.concurrent.Executor

@Configuration
class AsyncConfig(private val customAsyncExceptionHandler: CustomAsyncExceptionHandler) : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 2
        executor.maxPoolSize = 2
        executor.queueCapacity = 500
        executor.setThreadNamePrefix("AsyncThread-")
        executor.initialize()
        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return customAsyncExceptionHandler
    }
}

@Component
class CustomAsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    private val logger = LoggerFactory.getLogger(CustomAsyncExceptionHandler::class.java)

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        logger.error("Exception message - ${ex.message}")
        logger.error("Method name - ${method.name}")
        for (param in params) {
            logger.error("Parameter value - $param")
        }
    }
}