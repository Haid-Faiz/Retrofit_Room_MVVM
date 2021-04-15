package com.example.api.network.responses

data class User(
    val access_token: String?,  // made it nullable becoz we are using same SignInResponse to get current user & we won't get the auth token on a GET request of current user.
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val name: String,
    val updated_at: String
)
