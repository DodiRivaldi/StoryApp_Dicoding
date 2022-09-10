package com.dodi.cerita.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dodi.cerita.abstraction.network.ApiService
import com.dodi.cerita.data.local.db.CeritaDb
import com.dodi.cerita.data.local.db.CeritaItem
import com.dodi.cerita.data.local.db.KeyItem
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class CeritaDataMediator(
    private val database: CeritaDb,
    private val apiService: ApiService,
    private val token: String
) : RemoteMediator<Int, CeritaItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CeritaItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val keys = getKeyCurrentPos(state)
                keys?.next?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val key = getKeyFirstItem(state)
                val prev =
                    key?.prev ?: return MediatorResult.Success(endOfPaginationReached = key != null)
                prev
            }
            LoadType.APPEND -> {
                val key = getKeyLastItem(state)
                val next =
                    key?.next ?: return MediatorResult.Success(endOfPaginationReached = key != null)
                next
            }
        }

        try {
            val response = apiService.getStory(token, page, state.config.pageSize, null)
            val endOfPage = response.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.keyDao().deleteKey()
                    database.ceritaDao().deleteAllCerita()
                }

                val prev = if (page == 1) null else page - 1
                val next = if (endOfPage) null else page + 1
                val keys = response.listStory.map {
                    KeyItem(id = it.id, prev = prev, next = next)
                }

                database.keyDao().insertKey(keys)
                response.listStory.forEach {
                    val cerita = CeritaItem(
                        it.id,
                        it.name,
                        it.description,
                        it.photoUrl,
                        it.createdAt,
                        it.lat,
                        it.lon
                    )
                    database.ceritaDao().insertCerita(cerita)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPage)
        } catch (ex: Exception) {
            return MediatorResult.Error(ex)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    private suspend fun getKeyFirstItem(state: PagingState<Int, CeritaItem>): KeyItem? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.keyDao().getKeyById(data.id)
        }
    }

    private suspend fun getKeyCurrentPos(state: PagingState<Int, CeritaItem>): KeyItem? {
        return state.anchorPosition?.let { pos ->
            state.closestItemToPosition(pos)?.id?.let { id ->
                database.keyDao().getKeyById(id)
            }
        }
    }

    private suspend fun getKeyLastItem(state: PagingState<Int, CeritaItem>): KeyItem? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.keyDao().getKeyById(data.id)
        }
    }
}