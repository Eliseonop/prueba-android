package com.tcontur.login_tcontur.ui.data

import com.tcontur.login_tcontur.ui.data.models.Boleto
import com.tcontur.login_tcontur.ui.data.models.EmpresaModel
import com.tcontur.login_tcontur.ui.data.models.LoginRequest
import com.tcontur.login_tcontur.ui.data.models.LoginResponse
import com.tcontur.login_tcontur.ui.data.models.Paradero
import com.tcontur.login_tcontur.ui.data.models.Ruta
import com.tcontur.login_tcontur.ui.data.models.Tarifa
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/api/token-auth")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/api/boletos")
    fun getBoletos(
        @Header("Authorization") authToken: String
    ): Call<List<Boleto>>

    @GET("/tracker/empresas")
    fun getEmpresas(): Call<List<EmpresaModel>>

    @GET("/api/tarifas")
    fun geTarifas(
        @Header("Authorization") authToken: String
    ): Call<List<Tarifa>>

    @GET("/api/paraderos")
    fun getParaderos(
        @Header("Authorization") authToken: String
    ): Call<List<Paradero>>

    @GET("/api/rutas")
    fun getRutas(
        @Header("Authorization") authToken: String
    ): Call<List<Ruta>>
}