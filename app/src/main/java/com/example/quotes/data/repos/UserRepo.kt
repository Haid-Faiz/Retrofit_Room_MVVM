package com.example.quotes.data.repos

import com.example.api.network.ApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepo {

    private var apiService: ApiService = ApiService.invoke() // OR-> ApiService()

    fun userSignIn(email: String, password: String): String {
        var msg: String = ""

        apiService.userSignIn(email, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful)
                        msg = response.body()?.string().toString()
                    else
                        msg = response.errorBody()?.string().toString()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    msg = t.message.toString()
                }
            })

        return msg
    }
}