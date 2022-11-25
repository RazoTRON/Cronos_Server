package com.data.controller

import com.domain.service.PeopleService
import com.domain.models.*
import com.domain.models.request.get.*
import com.domain.use_case.GetPeopleUseCase
import com.domain.use_case.GetPhoneUseCase
import org.koin.java.KoinJavaComponent.inject
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class PeopleServiceImpl(
    db: CoroutineDatabase
) : PeopleService {

    val getPeopleUseCase: GetPeopleUseCase by inject(GetPeopleUseCase::class.java)
    val getPhoneUseCase: GetPhoneUseCase by inject(GetPhoneUseCase::class.java)

    val PE = db.getCollection<People>()
    val PH = db.getCollection<Phone>()
    val PS = db.getCollection<Passport>()
    val AD = db.getCollection<Address>()
    val AN = db.getCollection<Anketa>()
    val PC = db.getCollection<Phone_Connector>("phone_connector")
    val USER = db.getCollection<User>()



    override suspend fun newPeople(people: People) {
        PE.insertOne(people)
    }

    override suspend fun newPeopleList(peopleList: List<People>) {
        PE.insertMany(peopleList)
    }

    override suspend fun newPhoneList(phoneList: List<Phone>) {
        PH.insertMany(phoneList)
    }

    override suspend fun newAddressList(addressList: List<Address>) {
        AD.insertMany(addressList)
    }

    override suspend fun newPassportList(passportList: List<Passport>) {
        PS.insertMany(passportList)
    }

    override suspend fun newAnketaList(anketaList: List<Anketa>) {
        AN.insertMany(anketaList)
    }

    override suspend fun newPhoneConnectorList(phoneConnectorList: List<Phone_Connector>) {
        PC.insertMany(phoneConnectorList)
    }

    override suspend fun newUser(user: User) {
        USER.insertOne(user)
    }

    override suspend fun getUserByUsername(findUserByUsernameRequest: FindUserByUsernameRequest): User? {
        return USER.findOne(User::username eq findUserByUsernameRequest.username)
    }

    override suspend fun getPeople(peoplesRequest: FindPeoplesRequest): List<People?> {
        return getPeopleUseCase.invoke(peoplesRequest)
    }

    override suspend fun getPhone(phoneRequest: FindPhoneRequest): List<Phone> {
        return getPhoneUseCase.invoke(phoneRequest)
    }

    override suspend fun getAddress(addressRequest: FindAddressRequest): List<Address> {
        return AD.find(Address::id eq addressRequest.id).limit(20).toList()
    }

    override suspend fun getPassport(passportRequest: FindPassportRequest): List<Passport> {
        return PS.find(Passport::id eq passportRequest.id).limit(20).toList()
    }

    override suspend fun getAnketa(anketaRequest: FindAnketaRequest): List<Anketa> {
        return AN.find(Anketa::id eq anketaRequest.id).limit(20).toList()
    }

//    override suspend fun getPhoneConnector(phoneConnectorRequest: FindPhoneConnectorRequest): List<Phone_Connector> {
//        return PC.find(Phone_Connector::peopleId eq phoneConnectorRequest.peopleId).limit(20).toList()
//    }


}