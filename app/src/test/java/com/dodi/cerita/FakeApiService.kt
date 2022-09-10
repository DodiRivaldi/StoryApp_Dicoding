package com.dodi.cerita

import com.dodi.cerita.abstraction.network.ApiService
import com.dodi.cerita.data.remote.response.CeritaResponse
import com.dodi.cerita.data.remote.response.DefaultResponse
import com.dodi.cerita.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummySignInResponse = DummyData.loginResponse()

    override suspend fun getStory(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): CeritaResponse {
        TODO("Not yet implemented")
    }

    override suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): DefaultResponse {
        TODO("Not yet implemented")
    }

    override suspend fun register(name: String, email: String, password: String): DefaultResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return dummySignInResponse
    }

}