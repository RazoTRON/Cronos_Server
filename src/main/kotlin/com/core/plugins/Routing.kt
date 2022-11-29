package com.core.plugins

import com.server.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        findPeopleRoutes()
        createPeopleRoutes()
        signUp()
        signIn()
        noAuthRoutes()
        authenticate()
        refreshToken()
    }
}
