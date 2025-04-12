package com.tcontur.login_tcontur.ui.data

import com.tcontur.login_tcontur.ui.data.models.LoginRequest
import com.tcontur.login_tcontur.ui.data.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/token-auth")  // Ajusta la URL según tu backend
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}