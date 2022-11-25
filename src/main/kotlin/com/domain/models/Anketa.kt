package com.domain.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Anketa(
    val id: String,
    val familyStatus: String,
    val education: String,
    val children: String,
    val workStatus: String,
    val workCategory: String,
    val workPosition: String,
    val workCompany: String,
    val living: String,
    @BsonId
    val bson_id: String = ObjectId().toString()
)