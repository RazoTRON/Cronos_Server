package com.domain.models.request.get

data class FindPeoplesRequest(
    val startId: String,
    val id: String,
    val enum_id: String,
    val phone: String,
    val name: String,
    val surname: String,
    val middleName: String,
    val dateOfBirthday: String,
    val key: String,
    val inn: String
)