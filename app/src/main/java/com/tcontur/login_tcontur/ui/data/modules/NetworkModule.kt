//package com.tcontur.login_tcontur.ui.data.modules
//
//import com.tcontur.login_tcontur.ui.data.AuthService
//import com.tcontur.login_tcontur.ui.data.BoletoService
//import com.tcontur.login_tcontur.ui.data.EmpresaService
//import com.tcontur.login_tcontur.ui.data.interceptor.AuthInterceptor
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkModule {
//
//    private const val BASE_URL = "https://urbanito-23lnu3rcea-uc.a.run.app"
//
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
//        val logging = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//        return OkHttpClient.Builder()
//            .addInterceptor(authInterceptor)
//            .addInterceptor(logging)
//            .build()
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(client: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL) // Puedes cambiar dinámicamente si querés
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Provides
//    fun provideBoletoService(retrofit: Retrofit): BoletoService {
//        return retrofit.create(BoletoService::class.java)
//    }
//
//    @Provides
//    fun provideEmpresaService(retrofit: Retrofit): EmpresaService {
//        return retrofit.create(EmpresaService::class.java)
//    }
//
//    @Provides
//    fun provideAuthService(retrofit: Retrofit): AuthService {
//        return retrofit.create(AuthService::class.java)
//    }
//}
