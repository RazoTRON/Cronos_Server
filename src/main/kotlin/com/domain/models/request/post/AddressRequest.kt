package com.domain.models.request.post

data class AddressRequest(
    val id: String = "",
    val region: String = "",
    val city: String = "",
    val postal: String = "",
    val address: String = "",
)