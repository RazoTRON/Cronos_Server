package com.server.routes

import com.core.util.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.noAuthRoutes() {

    get("/status") {
        if (Constants.REGISTER) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}