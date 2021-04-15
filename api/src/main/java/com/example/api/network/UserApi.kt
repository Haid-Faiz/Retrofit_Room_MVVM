package com.example.api.network

import com.example.api.network.responses.SignInResponse
import retrofit2.http.GET

interface UserApi {

    @GET("user")
    suspend fun getUser() : SignInResponse
}