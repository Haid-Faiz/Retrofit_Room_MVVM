package com.example.quotes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.api.network.ApiClient
import com.example.api.network.Resource
import com.example.api.network.UserApi
import com.example.api.network.responses.User
import com.example.quotes.data.repos.UserRepo
import com.example.quotes.databinding.FragmentHomeBinding
import com.example.quotes.ui.base.BaseFragment
import com.example.quotes.utils.handleApiError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel, UserRepo>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel.getUser()
        _viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    // TODO hide progress bar
                    updateUI(it.value.user)
                }
                is Resource.Failure -> {
                    // TODO handle failure
                    handleApiError(it)
                }
                is Resource.Loading -> {
                    // TODO Show progress bar
                }
            }
        }
    }

    private fun updateUI(user: User) {

    }

    override fun getViewModel(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun getRepo(): UserRepo {
        val authToken =
            runBlocking { userPreferences.authToken.first() } // We shouldn't call runBlocking{} as it can cause the ANR
        val userApi = ApiClient.getApiService<UserApi>(UserApi::class.java, authToken)
        return UserRepo(userApi)
    }

}