package com.domain.use_case

import com.domain.models.Passport
import com.domain.models.People
import com.domain.models.Phone
import com.domain.models.Phone_Connector
import com.domain.models.request.get.FindPeoplesRequest
import com.domain.models.request.get.FindPhoneRequest
import org.bson.conversions.Bson
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.util.concurrent.TimeUnit

class GetPeopleUseCase(val db: CoroutineDatabase) {
    suspend fun invoke(peoplesRequest: FindPeoplesRequest): List<People> {

        val PE = db.getCollection<People>()
        val PH = db.getCollection<Phone>()
        val PS = db.getCollection<Passport>()
        val PC = db.getCollection<Phone_Connector>("phone_connector")

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
            if (!peoplesRequest.phone.contains('*')) {
                val phoneIdArgs = PH.find(Phone::phone eq peoplesRequest.phone).limit(20).toList()

                if (phoneIdArgs.isNotEmpty()) {
                    val args = phoneIdArgs.map { Phone_Connector::phoneId eq it.id }
                    val phoneConnectorArgs = PC
                        .find(or(args))
                        .limit(20)
                        .maxTime(5000L, TimeUnit.MILLISECONDS)
                        .toList()
                        .map { People::id eq it.peopleId }
                    simpleArgs.addAll(phoneConnectorArgs)
                } else {
                    return emptyList()
                }
            }
                    val phoneIdArgs =
                        PH.find(Phone::phone.regex("^${peoplesRequest.phone.replace("*", ".*")}$")).limit(100).toList()

                    if (phoneIdArgs.isNotEmpty()) {
                        val args = phoneIdArgs.map { Phone_Connector::phoneId eq it.id }
                        val phoneConnectorArgs = PC
                            .find(and(Phone_Connector::id gt peoplesRequest.id, or(args)))
                            .sort(ascending(Phone_Connector::peopleId))
                            .limit(20)
                            .maxTime(5000L, TimeUnit.MILLISECONDS)
                            .toList()
                            .map { People::id eq it.peopleId }
                        simpleArgs.addAll(listOf(or(phoneConnectorArgs)))
                    } else {
                        return emptyList()
                    }
        }

        if (peoplesRequest.inn.isNotBlank()) {
            if (!peoplesRequest.inn.contains('*')) {
                val innIdArgs = PS.find(Passport::inn eq peoplesRequest.inn).limit(20).toList()
                val args = innIdArgs.map { People::id eq it.id }
                if (args.isNotEmpty()) {
                    simpleArgs.addAll(listOf(or(args)))
                } else {
                    return emptyList()
                }
            } else {
                if (listOf(
                        peoplesRequest.phone,
                        peoplesRequest.name,
                        peoplesRequest.surname,
                        peoplesRequest.middleName,
                        peoplesRequest.dateOfBirthday
                    ).all { it.isBlank() }
                ) {
                    val innIdArgs =
                        PS.find(
                            and(
                                Passport::id gt peoplesRequest.id,
                                Passport::inn.regex("^${peoplesRequest.inn.replace("*", ".*")}$")
                            )
                        ).sort(ascending(Passport::id)).limit(20).toList()
                    val args = innIdArgs.map { People::id eq it.id }
                    if (args.isNotEmpty()) {
                        simpleArgs.addAll(listOf(or(args)))
                    } else {
                        return emptyList()
                    }
                }
            }
        }

        val startIdArg = listOf(People::id gt peoplesRequest.id)

        val argsList = simpleArgs + startIdArg + regexArgs

        var findList: List<People> = PE.find(and(argsList))
            .sort(ascending(People::id))
            .limit(20)
            .maxTime(5000L, TimeUnit.MILLISECONDS)
            .toList()

        findList = findList.filter { people ->
            if (peoplesRequest.phone.contains('*')) {
                GetPhoneUseCase(db).invoke(FindPhoneRequest("", people.id, ""))
                    .any { phone -> phone.phone.contains("^${peoplesRequest.phone.replace("*", "\\d*")}$".toRegex()) }
            } else {
                true
            }
        }.filter {
            if (peoplesRequest.inn.contains('*')) {
                PS.find(Passport::id eq it.id).limit(20).maxTime(5000L, TimeUnit.MILLISECONDS).toList()
                    .any { passport ->
                        passport.inn.contains("^${peoplesRequest.inn.replace("*", "\\d*")}$".toRegex())
                    }
            } else {
                true
            }
        }

        findList.forEach { println("${it.surname} ${it.name} ${it.middle_name}") }

        return findList
    }
}

