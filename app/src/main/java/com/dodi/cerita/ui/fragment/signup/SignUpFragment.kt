package com.dodi.cerita.ui.fragment.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.dodi.cerita.R
import com.dodi.cerita.abstraction.showProgressBar
import com.dodi.cerita.abstraction.showToast
import com.dodi.cerita.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private val viewModel : SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignup.setOnClickListener {
            signUp(binding.edtUsername.text.toString(),binding.edtEmail.text.toString(),binding.edtPassword.text.toString())
        }
    }

    private fun signUp(name : String, email : String, password : String){
        showProgressBar(true,binding.pbSignup)
        viewModel.viewModelScope.launch {
            viewModel.signUp(name, email, password).collect{value->
                value.onSuccess { findNavController().navigate(R.id.action_signUpFragment_to_signInFragment) }
                when{
                    name.isEmpty()->{
                        showToast(requireContext(),getString(R.string.username_empty))
                        showProgressBar(false,binding.pbSignup)
                    }
                    email.isEmpty()->{
                        showToast(requireContext(),getString(R.string.email_empty))
                        showProgressBar(false,binding.pbSignup)
                    }
                    password.isEmpty()->{
                        showToast(requireContext(),getString(R.string.password_empty))
                        showProgressBar(false,binding.pbSignup)
                    }
                    else->{
                        value.onFailure {
                            showToast(requireContext(),getString(R.string.usernameemailpassword_empty))
                            showProgressBar(false,binding.pbSignup)
                        }
                    }
                }
            }
        }
    }
}