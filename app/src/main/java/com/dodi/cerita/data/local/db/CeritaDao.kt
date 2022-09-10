package com.dodi.cerita.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CeritaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCerita(item: CeritaItem)

    @Query("SELECT * FROM cerita")
    fun getAllCerita(): PagingSource<Int, CeritaItem>

    @Query("DELETE FROM cerita")
    suspend fun deleteAllCerita()
}