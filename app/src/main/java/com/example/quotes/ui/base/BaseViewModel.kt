package com.example.quotes.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.network.UserApi
import com.example.quotes.data.repos.BaseRepo
import kotlinx.coroutines.launch

abstract class BaseViewModel(private val baseRepo: BaseRepo) : ViewModel() {

    fun logout(userApi: UserApi) = viewModelScope.launch {
        baseRepo.logout(userApi)
    }
}