package com.server.routes

import com.domain.service.PeopleService
import com.domain.models.request.get.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.findPeopleRoutes() {
    val peopleService: PeopleService by inject()

    route("/api/user/find") {
        get {
            val user = peopleService.getUserByUsername(
                FindUserByUsernameRequest(
                    username = call.request.queryParameters["username"] ?: ""
                )
            )
            call.respond(listOfNotNull(user))
        }
    }

    route("/api/people/find") {
        authenticate {
            get {
                val list = peopleService.getPeople(
                    FindPeoplesRequest(
                        startId = call.request.queryParameters["startId"] ?: "",
                        id = call.request.queryParameters["id"] ?: "",
                        enum_id = call.request.queryParameters["enum_id"] ?: "",
                        phone = call.request.queryParameters["phone"] ?: "",
                        name = call.request.queryParameters["name"] ?: "",
                        surname = call.request.queryParameters["surname"] ?: "",
                        middleName = call.request.queryParameters["middleName"] ?: "",
                        dateOfBirthday = call.request.queryParameters["dateOfBirthday"] ?: "",
                        key = call.request.queryParameters["key"] ?: "",
                        inn = call.request.queryParameters["inn"] ?: ""
                    )
                )
                call.respond(list)
            }
        }
    }

    route("/api/passport/find") {
        authenticate {
            get {
                val passport = peopleService.getPassport(
                    FindPassportRequest(
                        id = call.request.queryParameters["id"] ?: ""
                    )
                )
                println("Passport: $passport")
                call.respond(passport)
            }
        }
    }

    route("/api/address/find") {
        authenticate {
            get {
                val address = peopleService.getAddress(
                    FindAddressRequest(
                        id = call.request.queryParameters["id"] ?: ""
                    )
                )
                println("Address: $address")
                call.respond(address)
            }
        }
    }

    route("/api/anketa/find") {
        authenticate {
            get {
                val anketa = peopleService.getAnketa(
                    FindAnketaRequest(
                        id = call.request.queryParameters["id"] ?: ""
                    )
                )
                println("Anketa: $anketa")
                call.respond(anketa)
            }
        }
    }
}