package com.example.quotes.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.api.network.Resource
import com.example.quotes.R
import com.example.quotes.data.repos.AuthRepo
import com.example.quotes.databinding.FragmentSignInBinding
import com.example.quotes.ui.base.BaseFragment
import com.example.quotes.utils.handleApiError

class SignInFragment : BaseFragment<FragmentSignInBinding, AuthViewModel, AuthRepo>() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _viewModel.liveSignInResponse.observe(viewLifecycleOwner) {
            // From this line only ProgressBar visibility has been handeled
            _binding.progressBar.isVisible = it is Resource.Loading
            when (it) {
                is Resource.Success -> _viewModel.saveAuthToken(it.value.user.access_token!!) // TODO Goto Home Screen
                is Resource.Failure -> handleApiError(it) { signIn() }
            }
        }

        _binding.buttonSignIn.setOnClickListener {
            signIn()
        }

        _binding.textViewSignUp.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun signIn() {
        _viewModel.signInUser(
            // TODO Add Input Validations
            _binding.editTextEmail.text.toString().trim(),
            _binding.editTextPassword.text.toString().trim()
        )
    }

    override fun getViewModel() = AuthViewModel::class.java
//    override fun getViewModel(): Class<AuthViewModel> = AuthViewModel::class.java
    // not necessary to mention return type

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignInBinding.inflate(inflater, container, false)

    override fun getRepo() =
        AuthRepo(
            apiService,
            userPreferences
        )  // This apiService object is coming from it's parent class i.e. BaseFragment

}



//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
//        _binding?.authViewModel = authViewModel
//        authViewModel.authListener = this
//}
