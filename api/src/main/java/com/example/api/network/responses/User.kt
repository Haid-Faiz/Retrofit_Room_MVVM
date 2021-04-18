package com.example.api.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "access_token")
    val access_token: String?,  // made it nullable becoz we are using same SignInResponse to get current user & we won't get the auth token on a GET request of current user.
    @Json(name = "created_at")
    val created_at: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "email_verified_at")
    val email_verified_at: Any,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "updated_at")
    val updated_at: String
)
