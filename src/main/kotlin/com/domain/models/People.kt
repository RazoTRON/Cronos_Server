package com.domain.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class People(
     val enum_id: String = "",
     val id: String = "",
     val ecb_id: String = "",
     val login: String = "",
     val surname: String = "",
     val name: String = "",
     val middle_name: String = "",
     val dateB: String = "",
     val key: String = "",
     val phone_conn: Array<String> = arrayOf(),
     val address_conn:  Array<String> = arrayOf(),
     val passport_conn:  Array<String> = arrayOf(),
     val anketa_conn:  Array<String> = arrayOf(),
    @BsonId
     val bson_id: String = ObjectId().toString(),
)