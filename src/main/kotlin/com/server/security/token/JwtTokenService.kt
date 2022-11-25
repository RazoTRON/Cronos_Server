package com.server.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService : TokenService {
    override fun generateAccessToken(tokenConfig: TokenConfig, vararg tokenClaim: TokenClaim): String {
        val accessToken = JWT.create()
            .withIssuer(tokenConfig.issuer)
            .withAudience(tokenConfig.audience)
            .withExpiresAt((Date(System.currentTimeMillis() + tokenConfig.accessTokenExpiredIn)))
        tokenClaim.forEach { accessToken.withClaim(it.name, it.value) }
        return accessToken.sign(Algorithm.HMAC256(tokenConfig.secret))
    }

    override fun generateRefreshToken(tokenConfig: TokenConfig, vararg tokenClaim: TokenClaim): String {
        val refreshToken = JWT.create()
            .withIssuer(tokenConfig.issuer)
            .withAudience(tokenConfig.audience)
            .withExpiresAt((Date(System.currentTimeMillis() + tokenConfig.refreshTokenExpiredIn)))
        tokenClaim.forEach { refreshToken.withClaim(it.name, it.value) }
        return refreshToken.sign(Algorithm.HMAC256(tokenConfig.secret))
    }
}