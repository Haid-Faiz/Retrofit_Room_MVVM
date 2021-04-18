package com.example.quotes.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.api.network.ApiClient
import com.example.api.network.ApiService
import com.example.api.network.UserApi
import com.example.quotes.data.UserPreferences
import com.example.quotes.data.repos.BaseRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel, R : BaseRepo> : Fragment() {

    protected lateinit var userPreferences: UserPreferences
    protected lateinit var _binding: B
    protected var apiService: ApiService = ApiService()
    lateinit var _viewModel: VM
    protected lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        _binding = getBinding(inflater, container)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
//        navController = Navigation.findNavController(view)
        userPreferences = UserPreferences(requireContext())
        lifecycleScope.launch { userPreferences.authToken.first() }
        val factory = MyViewModelFactory(getRepo())
        _viewModel = ViewModelProvider(this, factory).get(getViewModel())
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getRepo(): R

    fun logout() {
        lifecycleScope.launch {
            val accessToken = userPreferences.authToken.first()
            _viewModel.logout(ApiClient.getApiService(UserApi::class.java, accessToken))
            // now we need to clear authToken from the local storage also
            userPreferences.clearAuthToken()
            // TODO now to goto SignIn Fragment
        }
    }
}