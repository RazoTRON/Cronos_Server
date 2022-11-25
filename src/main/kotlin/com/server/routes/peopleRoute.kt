package com.server.routes

import com.domain.service.PeopleService
import com.domain.models.*
import com.core.responses.BasicApiResponse
import com.core.util.Constants
import com.domain.models.request.get.*
import com.domain.models.request.post.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.peopleRoute() {
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
                        phone_conn = it.phone_conn,
                        address_conn = it.address_conn,
                        passport_conn = it.passport_conn,
                        anketa_conn = it.anketa_conn
                    )
                }
            )

            call.respond(BasicApiResponse(successful = true))
        }
    }
    route("/api/phone/create") {
        post {
            val request = call.receiveNullable<List<PhoneRequest>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            peopleService.newPhoneList(
                request.map {
                    Phone(
                        id = it.id,
                        phone = it.phone,
                    )
                }
            )

            call.respond(BasicApiResponse(successful = true))
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

            call.respond(BasicApiResponse(successful = true))
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
            call.respond(BasicApiResponse(successful = true))
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
            call.respond(BasicApiResponse(successful = true))
        }
    }

    route("/api/phone_connector/create") {
        post {
            val request = call.receiveNullable<List<PhoneConnectorRequest>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            peopleService.newPhoneConnectorList(
                request.map {
                    Phone_Connector(
                        id = it.id,
                        phoneId = it.phoneId,
                        peopleId = it.peopleId
                    )
                }
            )
            call.respond(BasicApiResponse(successful = true))
        }
    }

    route("/api/user/create") {
        post {
            val request = call.receiveNullable<NewUserRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            peopleService.newUser(request.toUser())
            call.respond(BasicApiResponse(successful = true))
        }
    }

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


    route("/api/phone/find") {
        authenticate {
            get {
                val phone = peopleService.getPhone(
                    FindPhoneRequest(
                        startId = call.request.queryParameters["startId"] ?: "",
                        peopleId = call.request.queryParameters["peopleId"] ?: "",
                        phone = call.request.queryParameters["phone"] ?: ""
                    )
                )
                println("Phone: $phone")
                call.respond(phone)
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



fun Route.reg() {
    get("/status") {
        if (Constants.REGISTER) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}