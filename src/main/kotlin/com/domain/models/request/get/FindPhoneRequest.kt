package com.domain.models.request.get

data class FindPhoneRequest(
    val startId: String,
    val peopleId: String,
    val phone: String,
)
