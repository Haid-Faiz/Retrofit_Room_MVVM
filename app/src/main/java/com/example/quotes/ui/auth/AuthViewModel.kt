package com.example.quotes.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.network.Resource
import com.example.api.network.responses.SignInResponse
import com.example.quotes.data.repos.AuthRepo
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepo: AuthRepo) : ViewModel() {

    private var _liveSignInResponse: MutableLiveData<Resource<SignInResponse>> = MutableLiveData()
    var liveSignInResponse: LiveData<Resource<SignInResponse>> = _liveSignInResponse

    fun signInUser(email: String, password: String) = viewModelScope.launch {
        _liveSignInResponse.postValue(Resource.Loading)
        _liveSignInResponse.postValue(authRepo.signIn(email, password))
    }

    fun saveAuthToken(authToken: String) = viewModelScope.launch {
        authRepo.saveAuthToken(authToken)
    }




    // =======================================
    // OLD CODE
//    var authListener: AuthListener? = null
//    var email: String? = null
//    var password: String? = null
//
//    private var _authLive = MutableLiveData<String>()
//    var authLive: LiveData<String> = _authLive
//
//    fun signIn() = viewModelScope.launch {
//        authListener?.onStarted()
//        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
//            authListener?.onFailure("Email or Pass is empty")
//            return@launch
//        }
//
//        val response = UserRepo.userSignIn(email!!, password!!)
//        _authLive.postValue(response)
//        authListener?.onSuccess(authLive)
//    }
}