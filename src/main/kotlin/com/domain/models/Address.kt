package com.domain.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Address(
    val id: String = "",
    val region: String = "",
    val city: String = "",
    val postal: String = "",
    val address: String = "",
    @BsonId
    val bson_id: String = ObjectId().toString()
)
