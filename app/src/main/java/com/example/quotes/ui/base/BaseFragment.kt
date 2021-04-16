package com.example.quotes.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.api.network.ApiClient
import com.example.api.network.ApiService
import com.example.quotes.data.UserPreferences
import com.example.quotes.data.repos.BaseRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel, R : BaseRepo> : Fragment() {

    protected lateinit var userPreferences: UserPreferences
    protected lateinit var _binding: B
    protected var apiService: ApiService = ApiService()
    lateinit var _viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        lifecycleScope.launch { userPreferences.authToken.first() }
        _binding = getBinding(inflater, container)
        val factory = MyViewModelFactory(getRepo())
        _viewModel = ViewModelProvider(this, factory).get(getViewModel())
        return _binding.root
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getRepo(): R
}