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
     val inn: String = "",
     val phoneList: List<String> = listOf(),
     @BsonId
     val bson_id: String = ObjectId().toString(),
)