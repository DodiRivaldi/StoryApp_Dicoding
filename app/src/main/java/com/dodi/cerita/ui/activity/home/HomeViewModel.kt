package com.dodi.cerita.ui.activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dodi.cerita.data.CeritaRepository
import com.dodi.cerita.data.local.db.CeritaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private var repo: CeritaRepository) : ViewModel() {
    fun getDataStory(token: String): LiveData<PagingData<CeritaItem>> {
        return repo.getStory(token).cachedIn(viewModelScope).asLiveData()
    }

    fun signOut() {
        viewModelScope.launch {
            repo.setToken("")
        }
    }

    fun getToken(): Flow<String?> = repo.getToken()

}