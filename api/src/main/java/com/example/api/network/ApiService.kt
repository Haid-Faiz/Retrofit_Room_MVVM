package com.example.api.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun userSignIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseBody>


    companion object {
        operator fun invoke(): ApiService {
            // if you are using operator keyword then function name should be invoke() only.
            return ApiClient.getClient()!!.create(ApiService::class.java)
        }
    }
    // Now with this operator invoke() function we can call this method like...
    // ApiService.invoke()  OR  ApiService()       {Note: -> ApiService is an interface}
    // But make sure that this invoke function is
    // present in companion object
}
