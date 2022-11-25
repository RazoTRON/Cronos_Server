package com.domain.models.request.post

data class PassportRequest(
    val id: String = "",
    val inn: String = "",
    val date: String = "",
    val passport: String = "",
    val by: String = "",
)