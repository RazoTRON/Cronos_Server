package com.server.di

import com.domain.use_case.GetPeopleUseCase
import com.domain.use_case.GetPhoneUseCase
import org.koin.dsl.module

val domainModule = module {
    single<GetPeopleUseCase> { GetPeopleUseCase(get()) }
    single<GetPhoneUseCase> { GetPhoneUseCase(get()) }
}