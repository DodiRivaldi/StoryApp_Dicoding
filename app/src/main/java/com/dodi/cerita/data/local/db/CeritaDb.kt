package com.dodi.cerita.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CeritaItem::class, KeyItem::class],
    version = 1, exportSchema = false
)

abstract class CeritaDb : RoomDatabase() {
    abstract fun ceritaDao(): CeritaDao
    abstract fun keyDao(): KeyDao
}