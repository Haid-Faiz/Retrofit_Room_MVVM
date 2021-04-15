package com.example.quotes.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.network.Resource
import com.example.api.network.responses.SignInResponse
import com.example.quotes.data.repos.UserRepo
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepo: UserRepo) : ViewModel() {

    private val _user: MutableLiveData<Resource<SignInResponse>> = MutableLiveData()
    val user: LiveData<Resource<SignInResponse>>
        get() = _user

    fun getUser() = viewModelScope.launch {
        _user.postValue(userRepo.getUser())
        _user.postValue(Resource.Loading)
    }
}