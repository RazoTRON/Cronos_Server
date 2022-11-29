package com.server.routes

import com.domain.service.PeopleService
import com.domain.models.*
import com.domain.models.request.post.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.createPeopleRoutes() {
    val peopleService: PeopleService by inject()
    route("/api/people/create") {
        post {
            val request = call.receiveNullable<List<PeopleRequest>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            peopleService.newPeopleList(
                request.map {
                    People(
                        enum_id = it.enum_id,
                        id = it.id,
                        ecb_id = it.ecb_id,
                        login = it.login,
                        name = it.name,
                        surname = it.surname,
                        middle_name = it.middle_name,
                        dateB = it.dateB,
                        key = it.key,
                    )
                }
            )

            call.respond(HttpStatusCode.OK)
        }
    }
    route("/api/address/create") {
        post {
            val request = call.receiveNullable<List<AddressRequest>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            peopleService.newAddressList(
                request.map {
                    Address(
                        id = it.id,
                        region = it.region,
                        city = it.city,
                        postal = it.postal,
                        address = it.address
                    )
                }
            )

            call.respond(HttpStatusCode.OK)
        }
    }

    route("/api/passport/create") {
        post {
            val request = call.receiveNullable<List<PassportRequest>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            peopleService.newPassportList(
                request.map {
                    Passport(
                        id = it.id,
                        inn = it.inn,
                        date = it.date,
                        passport = it.passport,
                        by = it.by
                    )
                }
            )
            call.respond(HttpStatusCode.OK)
        }
    }


    route("/api/anketa/create") {
        post {
            val request = call.receiveNullable<List<AnketaRequest>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            peopleService.newAnketaList(
                request.map {
                    Anketa(
                        id = it.id,
                        familyStatus = it.familyStatus,
                        education = it.education,
                        children = it.children,
                        workStatus = it.workStatus,
                        workCategory = it.workCategory,
                        workPosition = it.workPosition,
                        workCompany = it.workCompany,
                        living = it.living
                    )
                }
            )
            call.respond(HttpStatusCode.OK)
        }
    }
}