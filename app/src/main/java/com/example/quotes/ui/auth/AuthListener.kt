package com.example.quotes.ui.auth

import androidx.lifecycle.LiveData

interface AuthListener {
    fun onStarted()
    fun onSuccess(authLive: LiveData<String>)
    fun onFailure(message: String)
}