package com.server.routes

import com.core.responses.ApiResponse
import com.core.responses.AuthResponse
import com.core.responses.CronosResponse
import com.core.util.Constants
import com.domain.models.User
import com.domain.models.request.get.FindUserByUsernameRequest
import com.domain.models.request.post.AuthRequest
import com.domain.service.PeopleService
import com.server.security.hashing.HashingService
import com.server.security.hashing.SaltedHash
import com.server.security.token.TokenBlockList
import com.server.security.token.TokenClaim
import com.server.security.token.TokenConfig
import com.server.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.signIn() {
    val peopleService: PeopleService by inject()
    val hashingService: HashingService by inject()
    val tokenService: TokenService by inject()
    val tokenConfig: TokenConfig by inject()



    post("/signin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.username.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.Conflict, ApiResponse.Error("Empty username or password."))
            return@post
        }
        val user = peopleService.getUserByUsername(FindUserByUsernameRequest(request.username)) ?: kotlin.run {
            call.respond(HttpStatusCode.Conflict, ApiResponse.Error("Invalid username or password."))
            return@post
        }

        val isValid = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(user.password, user.salt)
        )

        if (!isValid) {
            call.respond(HttpStatusCode.Conflict, ApiResponse.Error("Invalid username or password."))
            return@post
        }

        val accessToken = tokenService.generateAccessToken(
            tokenConfig = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id
            )
        )

        val refreshToken = tokenService.generateRefreshToken(
            tokenConfig = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = ApiResponse.Success(
                data = AuthResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    userId = user.id
                )
            )
        )
    }


}

fun Route.signUp() {
    val hashingService: HashingService by inject()
    val peopleService: PeopleService by inject()

    post("/signup") {
        if (Constants.REGISTER) {
            val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (request.username.isBlank() || request.password.isBlank()) {
                call.respond(HttpStatusCode.Conflict, message = ApiResponse.Error(message = "Field is empty."))
                return@post
            }

            if (peopleService.getUserByUsername(FindUserByUsernameRequest(request.username)) != null) {
                call.respond(HttpStatusCode.Conflict, ApiResponse.Error(message = "User already exist."))
                return@post
            }

            val hash = hashingService.generateSaltedHash(request.password)
            peopleService.newUser(
                User(
                    username = request.username,
                    password = hash.hash,
                    salt = hash.salt
                )
            )
            call.respond(HttpStatusCode.OK, ApiResponse.Success())
        } else {
            call.respond(HttpStatusCode.NotFound, ApiResponse.Error(message = "Not found"))
        }
    }
}

fun Route.authenticate() {
    authenticate {
        get("/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.refreshToken() {
    authenticate {
        val peopleService: PeopleService by inject()
        val tokenService: TokenService by inject()
        val tokenConfig: TokenConfig by inject()

        post("/refresh_token") {
            call.request.headers["Authorization"]?.let(::println)
            val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            TokenBlockList.blockList.add(call.request.headers["Authorization"] ?: "")
//
//            if (request.username.isBlank()) {
//                call.respond(HttpStatusCode.Conflict)
//                return@get
//            }
//            val user = peopleService.getUserByUsername(FindUserByUsernameRequest(request.username)) ?: kotlin.run {
//                call.respond(HttpStatusCode.Conflict)
//                return@get
//            }
            val currentTime = System.currentTimeMillis()

            val accessToken = tokenService.generateAccessToken(
                tokenConfig = tokenConfig,
                TokenClaim(
                    name = "lastRefresh",
                    value = currentTime.toString()
                )
            )

            val refreshToken = tokenService.generateRefreshToken(
                tokenConfig = tokenConfig,
//                TokenClaim(
//                    name = "userId",
//                    value = user.id
//                ),
                TokenClaim(
                    name = "lastRefresh",
                    value = currentTime.toString() // TODO
                )
            )
            println("New refresh token: $refreshToken")
            call.respond(HttpStatusCode.OK, ApiResponse.Success(AuthResponse(accessToken = accessToken, refreshToken = refreshToken)))
        }
    }
}