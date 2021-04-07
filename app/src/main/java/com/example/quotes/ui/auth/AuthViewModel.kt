package com.example.quotes.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotes.data.repos.UserRepo
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var authListener: AuthListener? = null
    var email: String? = null
    var password: String? = null

    private var _authLive = MutableLiveData<String>()
    var authLive: LiveData<String> = _authLive

    fun signIn() = viewModelScope.launch {
        authListener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Email or Pass is empty")
            return@launch
        }

        val response = UserRepo.userSignIn(email!!, password!!)
        _authLive.postValue(response)
        authListener?.onSuccess(authLive)
    }
}