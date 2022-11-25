package com.server

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.core.plugins.*
import com.server.di.domainModule
import com.server.di.mainModule
import com.server.di.securityModule
import com.server.security.hashing.SHA256HashingService
import com.server.security.token.JwtTokenService
import com.server.security.token.TokenConfig
import io.ktor.server.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit {
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    val rootLogger = loggerContext.getLogger("org.mongodb.driver")
    rootLogger.setLevel(Level.OFF)

    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    install(Koin) {
        val tokenConfig = module { single<TokenConfig> { TokenConfig(environment) } }
        modules(listOf(mainModule, domainModule, securityModule, tokenConfig))
    }

    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
