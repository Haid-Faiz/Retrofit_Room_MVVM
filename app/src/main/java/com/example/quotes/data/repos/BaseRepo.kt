package com.example.quotes.data.repos

import com.example.api.network.Resource
import com.example.api.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepo {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {

            try {
                Resource.Success<T>(apiCall.invoke())
            } catch (throwable: Throwable) {

                when (throwable) {
                    is HttpException -> Resource.Failure(
                        false,
                        throwable.code(),
                        throwable.response()?.errorBody()
                    )

                    else -> Resource.Failure(true, null, null)
                }
            }
        }
    }

    suspend fun logout(userApi: UserApi) = safeApiCall { userApi.logout() }
}