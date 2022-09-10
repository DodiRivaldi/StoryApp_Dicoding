package com.dodi.cerita.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: List<KeyItem>)

    @Query("SELECT * FROM keyitems WHERE id = :id")
    suspend fun getKeyById(id: String): KeyItem?

    @Query("DELETE FROM keyitems")
    suspend fun deleteKey()
}