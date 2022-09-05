package com.dodi.cerita.ui.fragment.signup

import androidx.lifecycle.ViewModel
import com.dodi.cerita.data.CeritaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repo : CeritaRepository) : ViewModel(){
    suspend fun signUp(name : String,email : String, password : String) = repo.signUp(name, email, password)
}