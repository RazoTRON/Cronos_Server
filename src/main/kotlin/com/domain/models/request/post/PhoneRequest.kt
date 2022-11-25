package com.domain.models.request.post

data class PhoneRequest(
    val id: String = "",
    val phone: String = "",
    val viber: String = "",
    val verify: String = "",
    val hide: String = "",
    val people_conn: List<String> = listOf(),
)