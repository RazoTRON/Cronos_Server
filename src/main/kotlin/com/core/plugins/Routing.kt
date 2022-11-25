package com.core.plugins

import com.server.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        peopleRoute()
        signUp()
        signIn()
        reg()
        authenticate()
        refreshToken()
    }
    

//    routing {
//        get("/") {
//            call.respondText("Hello World!")
//        }
//        // Static plugin. Try to access `/static/index.html`
//        static("/static") {
//            resources("static")
//        }
//    }
}
