package com.domain.use_case

import com.domain.models.People
import com.domain.models.Phone
import com.domain.models.Phone_Connector
import com.domain.models.request.get.FindPeoplesRequest
import com.domain.models.request.get.FindPhoneRequest
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or

class GetPhoneUseCase(private val db: CoroutineDatabase) {
    suspend fun invoke(findPhoneRequest: FindPhoneRequest): List<Phone> {
        val PH = db.getCollection<Phone>()
        val PC = db.getCollection<Phone_Connector>("phone_connector")

        val phoneIdArgs = PC.find(Phone_Connector::peopleId eq findPhoneRequest.peopleId).limit(20).toList()

        return if (phoneIdArgs.isNotEmpty()){
            val ar = phoneIdArgs.map { Phone::id eq it.phoneId }
            PH.find(or(ar)).limit(20).toList()
        } else {
            emptyList()
        }
    }
//
}

class GetPeopleByPhone(private val db: CoroutineDatabase) {
    suspend fun invoke(findPeoplesRequest: FindPeoplesRequest): List<People> {
        val PH = db.getCollection<Phone>()
        val PC = db.getCollection<Phone_Connector>("phone_connector")
        val PE = db.getCollection<People>()

        val phoneIdArgs = PH.find(Phone::phone eq findPeoplesRequest.phone).limit(20).toList()

        return if (phoneIdArgs.isNotEmpty()) {
            val args = phoneIdArgs.map { Phone_Connector::peopleId eq it.id }
            val phoneConnectorArgs = PC.find(or(args)).limit(20).toList().map { People::id eq it.peopleId }
            PE.find(or(phoneConnectorArgs)).limit(20).toList()
        } else {
            emptyList()
        }
    }
}