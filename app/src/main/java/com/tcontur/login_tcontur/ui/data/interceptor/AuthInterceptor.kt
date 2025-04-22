//package com.tcontur.login_tcontur.ui.data.interceptor
//
//import kotlinx.coroutines.runBlocking
//import okhttp3.Interceptor
//import okhttp3.Response
//
//class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originalRequest = chain.request()
//
//        val token = runBlocking {
//            tokenProvider.getToken()
//        }
//
//        val newRequest = if (!token.isNullOrEmpty()) {
//            originalRequest.newBuilder()
//                .addHeader("Authorization", "Token $token")
//                .build()
//        } else {
//            originalRequest
//        }
//
//        return chain.proceed(newRequest)
//    }
//}
//
//
//
//
//
