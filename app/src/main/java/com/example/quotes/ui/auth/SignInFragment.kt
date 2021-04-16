package com.example.quotes.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.api.network.Resource
import com.example.quotes.data.repos.AuthRepo
import com.example.quotes.databinding.FragmentSignInBinding
import com.example.quotes.ui.base.BaseFragment
import com.example.quotes.utils.handleApiError

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class SignInFragment : BaseFragment<FragmentSignInBinding, AuthViewModel, AuthRepo>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _viewModel.liveSignInResponse.observe(viewLifecycleOwner) {
            // From this line only ProgressBar visibility has been handeled
            _binding.progressBar.isVisible = it is Resource.Loading
            when (it) {
                is Resource.Success -> _viewModel.saveAuthToken(it.value.user.access_token!!) // TODO Goto Home Screen
                is Resource.Failure -> handleApiError(it)
            }
        }

        _binding.buttonSignIn.setOnClickListener {
            _viewModel.signInUser(
                // TODO Add Input Validations
                _binding.editTextEmail.text.toString().trim(),
                _binding.editTextPassword.text.toString().trim()
            )
        }
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
//class SignInFragment : Fragment(), AuthListener {
//
//    private var _binding: FragmentSignInBinding? = null
//    private lateinit var authViewModel: AuthViewModel
//
//    override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        _binding = FragmentSignInBinding.inflate(inflater, container, false)
//        return _binding?.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
//        _binding?.authViewModel = authViewModel
//        authViewModel.authListener = this
//
////        view.findViewById<Button>(R.id.button_first).setOnClickListener {
////            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
////        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        _binding = null
//    }
//
//    override fun onStarted() {
//        Toast.makeText(requireContext(), "onStarted called", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onSuccess(authLive: LiveData<String>) {
//        authLive.observe(this) {
//            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onFailure(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }
//}