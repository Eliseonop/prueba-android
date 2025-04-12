package com.tcontur.login_tcontur.ui.data.models

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: Int,
    val message: String
)