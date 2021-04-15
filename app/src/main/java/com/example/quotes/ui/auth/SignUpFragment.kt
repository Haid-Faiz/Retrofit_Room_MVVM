package com.example.quotes.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.quotes.R
import com.example.quotes.data.repos.AuthRepo
import com.example.quotes.databinding.FragmentSignUpBinding
import com.example.quotes.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SignUpFragment : BaseFragment<FragmentSignUpBinding, AuthViewModel, AuthRepo>() {
    override fun getViewModel(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun getRepo(): AuthRepo = AuthRepo(apiService, userPreferences)

}