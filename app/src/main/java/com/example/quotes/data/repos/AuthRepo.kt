package com.example.quotes.data.repos

import com.example.api.network.ApiService
import com.example.quotes.data.UserPreferences


class AuthRepo(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) : BaseRepo() {

    suspend fun signIn(
        email: String,
        password: String
    ) = safeApiCall { apiService.signIn(email, password) }

    suspend fun saveAuthToken(authToken: String) {
        userPreferences.saveAuthToken(authToken)
    }

}