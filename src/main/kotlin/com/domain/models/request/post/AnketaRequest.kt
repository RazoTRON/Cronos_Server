package com.domain.models.request.post

data class AnketaRequest(
    val id: String = "",
    val familyStatus: String = "",
    val education: String = "",
    val children: String = "",
    val workStatus: String = "",
    val workCategory: String = "",
    val workPosition: String = "",
    val workCompany: String = "",
    val living: String = "",
)
