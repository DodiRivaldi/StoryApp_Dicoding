package com.dodi.cerita.ui.fragment.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dodi.cerita.data.CeritaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val repo : CeritaRepository) : ViewModel(){
    suspend fun signIn(email : String, password : String) = repo.signIn(email,password)

    fun saveToken(token : String){
        viewModelScope.launch {
            repo.setToken(token)
        }
    }
}