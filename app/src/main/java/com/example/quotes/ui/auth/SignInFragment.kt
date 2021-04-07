package com.example.quotes.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.quotes.databinding.FragmentSignInBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SignInFragment : Fragment(), AuthListener {

    private var _binding: FragmentSignInBinding? = null
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        _binding?.authViewModel = authViewModel
        authViewModel.authListener = this

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStarted() {
        Toast.makeText(requireContext(), "onStarted called", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(authLive: LiveData<String>) {
        authLive.observe(this) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}