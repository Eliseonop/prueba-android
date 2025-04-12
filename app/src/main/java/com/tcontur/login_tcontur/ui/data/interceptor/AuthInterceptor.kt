package com.tcontur.login_tcontur.ui.data.interceptor

import com.tcontur.login_tcontur.ui.core.login.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
//import com.tcontur.login_tcontur.ui.core.login.SessionManager
//import okhttp3.Interceptor
//import okhttp3.Response
//
class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Si no hay token, continuar sin modificar la request
        val token = sessionManager.getAuthToken()
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        // Agregar el header de autorización
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Token $token")
            .build()
        return chain.proceed(modifiedRequest)
    }
}


