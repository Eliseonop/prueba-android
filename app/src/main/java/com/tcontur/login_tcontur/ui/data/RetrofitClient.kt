package com.tcontur.login_tcontur.ui.data
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://urbanito-23lnu3rcea-uc.a.run.app"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log completo del request/response
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    private fun initRetrofit(newBaseUrl: String = BASE_URL): Retrofit {
        return Retrofit.Builder()
            .baseUrl(newBaseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(company: String): ApiService? {
        val baseUrl = "https://${company}-23lnu3rcea-uc.a.run.app"
        val retrofit = initRetrofit(baseUrl)

        try {
            return retrofit.create(ApiService::class.java)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return null
        }
    }

    fun getEmpresaService(): ApiService? {
        val retrofit = initRetrofit()
        try {
            return retrofit.create(ApiService::class.java)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return null
        }
    }

//    fun getBoletoService(company: String): ApiService? {
//        val baseUrl = "https://${company}-23lnu3rcea-uc.a.run.app"
//        Log.e("URL", baseUrl)
//        val retrofit = initRetrofit(baseUrl)
//        try {
//            return retrofit.create(ApiService::class.java)
//        } catch (e: IllegalArgumentException) {
//            e.printStackTrace()
//            return null
//        }
//    }
}
