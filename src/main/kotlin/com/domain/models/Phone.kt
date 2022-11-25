package com.domain.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Phone(
    val id: String = "",
    val phone: String = "",
    @BsonId
    val bson_id: String = ObjectId().toString(),
)