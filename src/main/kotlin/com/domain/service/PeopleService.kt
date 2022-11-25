package com.domain.service

import com.domain.models.*
import com.domain.models.request.get.*

interface PeopleService {
    suspend fun newPeople(people: People)
    suspend fun newPeopleList(peopleList: List<People>)
    suspend fun newPhoneList(phoneList: List<Phone>)
    suspend fun newAddressList(addressList: List<Address>)
    suspend fun newPassportList(passportList: List<Passport>)
    suspend fun newAnketaList(anketaList: List<Anketa>)
    suspend fun newPhoneConnectorList(phoneConnectorList: List<Phone_Connector>)
    suspend fun newUser(user: User)

    suspend fun getUserByUsername(findUserByUsernameRequest: FindUserByUsernameRequest): User?

    suspend fun getPeople(peoplesRequest: FindPeoplesRequest): List<People?>

    suspend fun getPhone(phoneRequest: FindPhoneRequest): List<Phone>
    suspend fun getAddress(addressRequest: FindAddressRequest): List<Address>
    suspend fun getPassport(passportRequest: FindPassportRequest): List<Passport>
    suspend fun getAnketa(anketaRequest: FindAnketaRequest): List<Anketa>
//    suspend fun getPhoneConnector(phoneConnectorRequest: FindPhoneConnectorRequest): List<Phone_Connector>
}