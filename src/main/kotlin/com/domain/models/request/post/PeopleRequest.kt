package com.domain.models.request.post

data class PeopleRequest(
    val enum_id: String = "",
    val id: String = "",
    val ecb_id: String = "",
    val login: String = "",
    val surname: String = "",
    val name: String = "",
    val middle_name: String = "",
    val dateB: String = "",
    val key: String = "",
    val phone_conn: Array<String> = arrayOf(),
    val address_conn: Array<String> = arrayOf(),
    val passport_conn: Array<String> = arrayOf(),
    val anketa_conn: Array<String> = arrayOf(),
)

