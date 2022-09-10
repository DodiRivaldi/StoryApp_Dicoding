package com.dodi.cerita.ui.activity.location

import androidx.lifecycle.ViewModel
import com.dodi.cerita.data.CeritaRepository
import com.dodi.cerita.data.remote.response.CeritaResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private var repo: CeritaRepository) : ViewModel() {
    fun getDataStory(token : String): Flow<Result<CeritaResponse>> =
        repo.getStoryLoc(token)

    fun getToken(): Flow<String?> = repo.getToken()
}