package com.domain.use_case

import com.domain.models.People
import com.domain.models.request.get.FindPeoplesRequest
import org.bson.conversions.Bson
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.util.concurrent.TimeUnit


class GetPeopleUseCase(val db: CoroutineDatabase) {
    suspend fun invoke(peoplesRequest: FindPeoplesRequest): List<People> {

        val PE = db.getCollection<People>()

        val simpleArgs = mutableListOf<Bson>()
        val regexArgs = mutableListOf<Bson>()

        if (peoplesRequest.surname.isNotBlank()) {
            if (peoplesRequest.surname.contains('*')) {
                regexArgs.add(People::surname.regex("^${peoplesRequest.surname.replace("*", ".*")}$"))
            } else {
                simpleArgs.add(People::surname eq peoplesRequest.surname)
            }
        }
        if (peoplesRequest.name.isNotBlank()) {
            if (peoplesRequest.name.contains('*')) {
                regexArgs.add(People::name.regex("^${peoplesRequest.name.replace("*", ".*")}$"))
            } else {
                simpleArgs.add(People::name eq peoplesRequest.name)
            }
        }
        if (peoplesRequest.middleName.isNotBlank()) {
            if (peoplesRequest.middleName.contains('*')) {
                regexArgs.add(People::middle_name.regex("^${peoplesRequest.middleName.replace("*", ".*")}$"))
            } else {
                simpleArgs.add(People::middle_name eq peoplesRequest.middleName)
            }
        }
        if (peoplesRequest.dateOfBirthday.isNotBlank()) {
            if (peoplesRequest.dateOfBirthday.contains('*')) {
                regexArgs.add(People::dateB.regex("^${peoplesRequest.dateOfBirthday.replace("*", ".*")}$")) // TODO
            } else {
                simpleArgs.add(People::dateB eq peoplesRequest.dateOfBirthday)
            }
        }

        if (peoplesRequest.phone.isNotBlank()) {
            if (peoplesRequest.phone.contains('*')) {
                regexArgs.add(People::phoneList.regex("^${peoplesRequest.phone.replace("*", ".*")}$")) // TODO
            } else {
                simpleArgs.add(People::phoneList `in` peoplesRequest.phone)
            }
        }


        if (peoplesRequest.inn.isNotBlank()) {
            if (peoplesRequest.inn.contains('*')) {
                regexArgs.add(People::inn.regex("^${peoplesRequest.inn.replace("*", ".*")}$")) // TODO
            } else {
                simpleArgs.add(People::inn eq peoplesRequest.inn)
            }
        }

        val startIdArg = listOf(People::id gt peoplesRequest.id)

        val argsList = simpleArgs + startIdArg + regexArgs

        val findList: List<People> = PE.find(and(argsList))
            .sort(ascending(People::id))
            .limit(20)
            .maxTime(5000L, TimeUnit.MILLISECONDS)
            .toList()

        findList.forEach { println("${it.surname} ${it.name} ${it.middle_name}") }

        return findList
    }
}