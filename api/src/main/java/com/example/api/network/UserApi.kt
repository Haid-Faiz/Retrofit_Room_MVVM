package com.example.api.network

import com.example.api.network.responses.SignInResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @GET("user")
    suspend fun getUser() : SignInResponse

    @POST("logout")
    suspend fun logout() : ResponseBody    // i don't want to know exact response so i am just
                                            // wrapping it out in ResponseBody
}