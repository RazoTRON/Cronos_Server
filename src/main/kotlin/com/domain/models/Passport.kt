package com.domain.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Passport(
    val id: String = "",
    val inn: String = "",
    val date: String = "",
    val passport: String = "",
    val by: String = "",
    @BsonId
    val bson_id: String = ObjectId().toString()
)
