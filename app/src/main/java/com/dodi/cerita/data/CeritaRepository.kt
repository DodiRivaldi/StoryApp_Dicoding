package com.dodi.cerita.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dodi.cerita.abstraction.Constant.RESPONSE_TAG
import com.dodi.cerita.abstraction.network.ApiService
import com.dodi.cerita.data.local.UserPref
import com.dodi.cerita.data.local.db.CeritaDb
import com.dodi.cerita.data.local.db.CeritaItem
import com.dodi.cerita.data.remote.response.CeritaResponse
import com.dodi.cerita.data.remote.response.DefaultResponse
import com.dodi.cerita.data.remote.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CeritaRepository @Inject constructor(
    private val apiService: ApiService,
    private val pref: UserPref,
    private val ceritaDb: CeritaDb
) {
    fun getToken(): Flow<String?> = pref.getToken()

    suspend fun setToken(token: String) {
        pref.setToken(token)
    }

    fun signUp(
        name: String,
        email: String,
        password: String
    ): Flow<Result<DefaultResponse>> = flow {
        try {
            val response = apiService.register(name, email, password)
            Log.d(RESPONSE_TAG, "SUCCES")
            emit(Result.success(response))
        } catch (ex: Exception) {
            Log.e(RESPONSE_TAG, "FAILED")
            emit(Result.failure(ex))
        }
    }

    fun signIn(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.login(email, password)
            Log.d(RESPONSE_TAG, "SUCCESS")
            emit(Result.success(response))
        } catch (ex: Exception) {
            Log.e(RESPONSE_TAG, "FAILED")
            emit(Result.failure(ex))
        }
    }

    fun getStory(token: String): Flow<PagingData<CeritaItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = CeritaDataMediator(ceritaDb, apiService, "Bearer $token"),
            pagingSourceFactory = { ceritaDb.ceritaDao().getAllCerita() }
        ).flow
    }

    fun uploadStory(
        token: String, file: MultipartBody.Part, description: RequestBody,
        lat: RequestBody?, lon: RequestBody?
    ): Flow<Result<DefaultResponse>> = flow {
        try {
            val token = "Bearer $token"
            val response = apiService.uploadStory(token, file, description, lat, lon)
            Log.d(RESPONSE_TAG, "SUCCESS")
            emit(Result.success(response))
        } catch (ex: Exception) {
            Log.e(RESPONSE_TAG, "FAILED")
            emit(Result.failure(ex))
        }
    }

    fun getStoryLoc(token: String): Flow<Result<CeritaResponse>> = flow {
        try {
            val bearerToken = "Bearer $token"
            val response = apiService.getStory(bearerToken,null, size = 100, location = 1)
            emit(Result.success(response))
        }catch (e : Exception){
            emit(Result.failure(e))
        }
    }
}