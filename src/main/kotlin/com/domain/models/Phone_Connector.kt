package com.domain.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Phone_Connector(
    val id: String,
    val peopleId: String,
    val phoneId: String,
    @BsonId
    val bson_id: String = ObjectId().toString()
)