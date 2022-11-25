package com.server.security.token

interface TokenService {
    fun generateAccessToken(tokenConfig: TokenConfig, vararg tokenClaim: TokenClaim): String
    fun generateRefreshToken(tokenConfig: TokenConfig, vararg tokenClaim: TokenClaim): String
}