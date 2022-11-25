package com.domain.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val username: String,
    val password: String,
    val salt: String,
    val lastRefresh: Int = 0,
    @BsonId val id: String = ObjectId().toString()
)
