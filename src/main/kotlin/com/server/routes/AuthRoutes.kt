package com.server.routes

import com.core.responses.Resource
import com.core.responses.AuthResponse
import com.core.util.Constants
import com.domain.models.User
import com.domain.models.request.get.FindUserByUsernameRequest
import com.domain.models.request.post.AuthRequest
import com.domain.models.request.post.RefreshTokenRequest
import com.domain.service.PeopleService
import com.server.security.hashing.HashingService
import com.server.security.hashing.SaltedHash
import com.server.security.token.*
import com.server.security.token.TokenBlockList.add
import com.server.security.token.TokenBlockList.get
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
            call.respond(HttpStatusCode.Conflict, Resource.Error("Empty username or password."))
            return@post
        }
        val user = peopleService.getUserByUsername(FindUserByUsernameRequest(request.username)) ?: kotlin.run {
            call.respond(HttpStatusCode.Conflict, Resource.Error("Invalid username or password."))
            return@post
        }

        val isValid = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(user.password, user.salt)
        )

        if (!isValid) {
            call.respond(HttpStatusCode.Conflict, Resource.Error("Invalid username or password."))
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
            message = Resource.Success(
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
                call.respond(HttpStatusCode.Conflict, message = Resource.Error(message = "Field is empty."))
                return@post
            }

            if (peopleService.getUserByUsername(FindUserByUsernameRequest(request.username)) != null) {
                call.respond(HttpStatusCode.Conflict, Resource.Error(message = "User already exist."))
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
            call.respond(HttpStatusCode.OK, Resource.Success())
        } else {
            call.respond(HttpStatusCode.NotFound, Resource.Error(message = "Not found"))
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
    val tokenService: TokenService by inject()
    val tokenConfig: TokenConfig by inject()

    post("/refresh_token") {
        val request = call.receiveNullable<RefreshTokenRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isVerified = tokenService.decodeToken(request.token).verify(tokenConfig)

        if (isVerified == null || TokenBlockList.get().contains(request.token)) {
            println("Unverified refresh token.")
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }

        TokenBlockList.add(request.token)

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
        call.respond(
            HttpStatusCode.OK,
            Resource.Success(AuthResponse(accessToken = accessToken, refreshToken = refreshToken))
        )
    }
}