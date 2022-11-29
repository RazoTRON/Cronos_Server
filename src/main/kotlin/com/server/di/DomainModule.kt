package com.server.di

import com.domain.use_case.GetPeopleUseCase
import org.koin.dsl.module

val domainModule = module {
    single<GetPeopleUseCase> { GetPeopleUseCase(get()) }
}