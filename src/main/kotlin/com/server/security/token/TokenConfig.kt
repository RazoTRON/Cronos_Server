package com.server.security.token

import io.ktor.server.application.*

data class TokenConfig(
    private val environment: ApplicationEnvironment
) {
    val issuer: String = environment.config.property("jwt.issuer").getString()
    val audience: String = environment.config.property("jwt.audience").getString()
    val secret: String = System.getenv("JWT_SECRET")
    val accessTokenExpiredIn: Long = 1000L * 100
    val refreshTokenExpiredIn: Long = 1000L * 10000
}