package com.domain.models.request.post

import com.domain.models.User

data class NewUserRequest(
    val username: String = "",
    val password: String = "",
    val salt: String = "",
)