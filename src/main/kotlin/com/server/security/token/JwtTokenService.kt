package com.server.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
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
    override fun decodeToken(token: String): DecodedJWT {
        return JWT.decode(token)
    }
}

fun DecodedJWT.verify(tokenConfig: TokenConfig, vararg tokenClaim: TokenClaim): DecodedJWT? {
    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(tokenConfig.secret))
        .withIssuer(tokenConfig.issuer)
        .withAudience(tokenConfig.audience)
        .build()

    return try { verifier.verify(this) } catch (e: Exception) { null } // TODO Exceptions
}