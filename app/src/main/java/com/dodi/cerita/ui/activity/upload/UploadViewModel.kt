package com.dodi.cerita.ui.activity.upload

import androidx.lifecycle.ViewModel
import com.dodi.cerita.data.CeritaRepository
import com.dodi.cerita.data.remote.response.DefaultResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val repo: CeritaRepository) : ViewModel() {
    fun getToken(): Flow<String?> = repo.getToken()

    suspend fun upload(
        token: String,
        desc: RequestBody,
        lat: RequestBody?,
        long: RequestBody?,
        image: MultipartBody.Part
    ): Flow<Result<DefaultResponse>> = repo.uploadStory(token, image, desc, lat, long)
}