package com.tcontur.login_tcontur.ui.data

import com.tcontur.login_tcontur.ui.data.viewmodel.boletos.Boleto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface BoletoService {
    @GET("/api/boletos") // Asegúrate de que este endpoint sea correcto
    fun getBoletos(
        @Header("Authorization") authToken: String
    ): Call<List<Boleto>>
}