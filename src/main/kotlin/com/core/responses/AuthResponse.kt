package com.core.responses

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val userId: String? = null
)

