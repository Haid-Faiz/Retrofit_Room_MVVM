package com.example.api.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {

        return retrofit ?: run {
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.simplifiedcoding.in/course-apis/mvvm/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            return retrofit
        }
    }
}