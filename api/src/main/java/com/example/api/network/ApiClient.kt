package com.example.api.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    private var retrofit: Retrofit? = null
    private const val BASE_URL = "http://simplifiedcoding.tech/mywebapp/public/api/"

    private fun getClient(authToken: String? = null): Retrofit? = retrofit ?: run {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().addInterceptor(
                    Interceptor { chain: Interceptor.Chain ->
                        val newRequest = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer $authToken")
                            .build()
                        chain.proceed(newRequest)
                    }
                ).build()  // this will return OkHttp client
            )
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()

        return retrofit
    }

    fun <MyApi> getApiService(api: Class<MyApi>, authToken: String? = null): MyApi =
        getClient(authToken)!!.create(api)
}