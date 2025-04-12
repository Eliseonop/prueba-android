package com.tcontur.login_tcontur.ui.data
import com.tcontur.login_tcontur.ui.data.models.EmpresaModel
import retrofit2.Call
import retrofit2.http.GET

interface EmpresaService {
    @GET("/tracker/empresas") // Asegúrate de que este endpoint sea correcto
    fun getEmpresas(): Call<List<EmpresaModel>>
}