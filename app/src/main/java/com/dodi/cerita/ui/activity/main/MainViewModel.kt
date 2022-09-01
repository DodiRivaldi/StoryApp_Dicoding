package com.dodi.cerita.ui.activity.main

import androidx.lifecycle.ViewModel
import com.dodi.cerita.data.CeritaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo : CeritaRepository): ViewModel() {
    fun getToken(): Flow<String?> = repo.getToken()
}