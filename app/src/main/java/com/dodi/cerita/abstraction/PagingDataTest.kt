package com.dodi.cerita.abstraction

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dodi.cerita.data.local.db.CeritaItem

class PagingDataTest : PagingSource<Int, LiveData<List<CeritaItem>>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<CeritaItem>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<CeritaItem>>> {
        return LoadResult.Page(emptyList(),0,1)
    }

    companion object{
        fun snapshot(item: List<CeritaItem>) : PagingData<CeritaItem>{
            return PagingData.from(item)
        }
    }
}