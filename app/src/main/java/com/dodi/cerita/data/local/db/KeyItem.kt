package com.dodi.cerita.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keyitems")
class KeyItem (
    @PrimaryKey
    val id :String,
    val prev : Int?,
    val next : Int?
)