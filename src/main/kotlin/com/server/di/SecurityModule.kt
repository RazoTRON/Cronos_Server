package com.server.di

import com.server.security.hashing.HashingService
import com.server.security.hashing.SHA256HashingService
import com.server.security.token.JwtTokenService
import com.server.security.token.TokenConfig
import com.server.security.token.TokenService
import org.koin.dsl.module

val securityModule = module {
    single<HashingService> { SHA256HashingService() }
    single<TokenService> { JwtTokenService() }
}