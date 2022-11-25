package com.server.di


import com.domain.service.PeopleService
import com.data.controller.PeopleServiceImpl
import com.core.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single<CoroutineDatabase> {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<PeopleService> {
        PeopleServiceImpl(get())
    }
}