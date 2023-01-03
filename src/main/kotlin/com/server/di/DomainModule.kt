package com.server.di

import com.domain.use_case.FindPeopleUseCase
import org.koin.dsl.module

val domainModule = module {
    single<FindPeopleUseCase> { FindPeopleUseCase(get()) }
}