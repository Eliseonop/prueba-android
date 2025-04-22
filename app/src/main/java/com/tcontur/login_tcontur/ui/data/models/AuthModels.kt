package com.tcontur.login_tcontur.ui.data.models

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val id: Int,
    val message: String,
    val email: String,
    val nombre: String,
    val empresa: String,
    val username: String
)