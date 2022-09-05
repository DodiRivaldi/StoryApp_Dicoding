package com.dodi.cerita.ui.fragment.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.dodi.cerita.R
import com.dodi.cerita.abstraction.showProgressBar
import com.dodi.cerita.abstraction.showToast
import com.dodi.cerita.databinding.FragmentSignInBinding
import com.dodi.cerita.ui.activity.home.HomeActivity
import com.dodi.cerita.ui.activity.home.HomeActivity.Companion.TOKEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel : SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignin.setOnClickListener{
            signIn(binding.edtEmail.text.toString(),binding.edtPassword.text.toString())

        }

        binding.tvSignup.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_signInFragment_to_signUpFragment, null));
    }

    private fun signIn(email : String, password : String){
        showProgressBar(true, binding.pbSignin)
        viewModel.viewModelScope.launch {
            viewModel.signIn(email,password).collect{
                it.onSuccess { value ->
                    value.loginResult.token.let { token->
                        viewModel.saveToken(token)
                        Intent(requireContext(),HomeActivity::class.java).also { intent->
                            intent.putExtra(TOKEN,token)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                }
                when{
                    email.isEmpty()->{
                        it.onFailure {
                            showToast(requireContext(),getString(R.string.email_empty))
                            showProgressBar(false,binding.pbSignin)
                        }
                    }
                    password.isEmpty()->{
                        it.onFailure {
                            showToast(requireContext(),getString(R.string.password_empty))
                            showProgressBar(false,binding.pbSignin)
                        }
                    }
                    else->{
                        it.onFailure {
                            showToast(requireContext(),getString(R.string.emailpassword_empty))
                            showProgressBar(false,binding.pbSignin)
                        }
                    }
                }
            }
        }
    }
}