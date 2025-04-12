package com.tcontur.login_tcontur.ui.data

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://urbanito-23lnu3rcea-uc.a.run.app"

//    val sessionManager = SessionManager(context)

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log completo del request/response
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

    private fun initRetrofit(newBaseUrl: String = BASE_URL): Retrofit {
        return Retrofit.Builder()
            .baseUrl(newBaseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAuthService(company: String): AuthService? {
        val baseUrl = "https://${company}-23lnu3rcea-uc.a.run.app"
        val retrofit = initRetrofit(baseUrl)



        try {
            return retrofit.create(AuthService::class.java)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return null
        }
    }


//    val authService: AuthService? by lazy {
//        try {
//            retrofit.create(AuthService::class.java)
//        } catch (e: IllegalArgumentException) {
//            e.printStackTrace()
//            null
//        }
//    }


//    val empresaService: EmpresaService? by lazy {
//        try {
//            retrofit.create(EmpresaService::class.java)
//        } catch (e: IllegalArgumentException) {
//            e.printStackTrace()
//            null
//        }
//    }
    fun getEmpresaService(): EmpresaService? {
        val retrofit = initRetrofit()
        try {
            return retrofit.create(EmpresaService::class.java)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return null
        }
    }




//    val boletoService: BoletoService? by lazy {
//        try {
//            retrofit.create(BoletoService::class.java)
//        } catch (e: IllegalArgumentException) {
//            e.printStackTrace()
//            null
//        }
//    }

    fun getBoletoService(company:String): BoletoService? {
        val baseUrl = "https://${company}-23lnu3rcea-uc.a.run.app"
        Log.e("URL", baseUrl)
        val retrofit = initRetrofit(baseUrl)
        try {
            return retrofit.create(BoletoService::class.java)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return null
        }
    }

}
