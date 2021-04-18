package com.example.api.network

import com.example.api.network.responses.SignInResponse
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): SignInResponse

    @FormUrlEncoded
    @POST("auth/signup")
    suspend fun signUp(
        @Field("email") email: String,
        @Field("password") password: String
    ): SignInResponse


    companion object {
        operator fun invoke(authToken: String? = null): ApiService {
            // if you are using operator keyword then function name should be invoke() only.
//            return ApiClient.getClient()!!.create(ApiService::class.java)
            return ApiClient.getApiService<ApiService>(ApiService::class.java, authToken)
        }
    }
    // Now with this operator invoke() function we can call this method like...
    // ApiService.invoke()  OR  ApiService()       {Note: -> ApiService is an interface}
    // But make sure that this invoke function is
    // present in companion object
}
