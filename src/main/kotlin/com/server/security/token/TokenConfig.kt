package com.server.security.token

import com.core.util.Constants.ACCESS_TOKEN_SEC
import com.core.util.Constants.REFRESH_TOKEN_SEC
import io.ktor.server.application.*

data class TokenConfig(
    private val environment: ApplicationEnvironment
) {
    val issuer: String = environment.config.property("jwt.issuer").getString()
    val audience: String = environment.config.property("jwt.audience").getString()
    val secret: String = System.getenv("JWT_SECRET")
    val accessTokenExpiredIn: Long = 1000L * ACCESS_TOKEN_SEC
    val refreshTokenExpiredIn: Long = 1000L * REFRESH_TOKEN_SEC
}