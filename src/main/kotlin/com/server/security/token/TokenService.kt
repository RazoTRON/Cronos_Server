package com.server.security.token

import com.auth0.jwt.interfaces.DecodedJWT

interface TokenService {
    fun generateAccessToken(tokenConfig: TokenConfig, vararg tokenClaim: TokenClaim): String
    fun generateRefreshToken(tokenConfig: TokenConfig, vararg tokenClaim: TokenClaim): String
    fun decodeToken(token: String): DecodedJWT
}