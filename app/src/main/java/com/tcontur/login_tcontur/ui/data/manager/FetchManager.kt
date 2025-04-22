package com.tcontur.login_tcontur.ui.data.manager

import android.util.Log
import com.tcontur.login_tcontur.ui.data.RetrofitClient
import com.tcontur.login_tcontur.ui.data.models.Boleto
import com.tcontur.login_tcontur.ui.data.models.EmpresaModel
import com.tcontur.login_tcontur.ui.data.models.LoginRequest
import com.tcontur.login_tcontur.ui.data.models.LoginResponse
import com.tcontur.login_tcontur.ui.data.models.Paradero
import com.tcontur.login_tcontur.ui.data.models.Ruta
import com.tcontur.login_tcontur.ui.data.models.Tarifa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchManager(
    private val sessionManager: SessionManager
) {
    private val _boletosFlow = MutableStateFlow<List<Boleto>>(emptyList())
    val boletosFlow: StateFlow<List<Boleto>> get() = _boletosFlow

    private val _empresasFlow = MutableStateFlow<List<EmpresaModel>>(emptyList())
    val empresasFlow: StateFlow<List<EmpresaModel>> get() = _empresasFlow

    private val _rutasFlow = MutableStateFlow<List<Ruta>>(emptyList())
    val rutasFlow: StateFlow<List<Ruta>> get() = _rutasFlow

    private val _paraderosFlow = MutableStateFlow<List<Paradero>>(emptyList())
    val paraderosFlow: StateFlow<List<Paradero>> get() = _paraderosFlow

    private val _tarifasFlow = MutableStateFlow<List<Tarifa>>(emptyList())
    val tarifasFlow: StateFlow<List<Tarifa>> get() = _tarifasFlow

    private val _paraderosFiltrados = MutableStateFlow<List<Paradero>>(emptyList())
    val paraderosFiltrados: StateFlow<List<Paradero>> get() = _paraderosFiltrados

    fun updateParaderosFiltrados(paraderos: List<Paradero>) {
        _paraderosFiltrados.value = paraderos
    }

    fun clearFlows() {
        _boletosFlow.value = emptyList()
        _rutasFlow.value = emptyList()
        _paraderosFlow.value = emptyList()
        _tarifasFlow.value = emptyList()
    }

    fun getEmpresas(
        onSuccess: (List<EmpresaModel>) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val service = RetrofitClient.getEmpresaService() ?: return onError("Error de red")
        if (_empresasFlow.value.isNotEmpty()) {
            onSuccess(_empresasFlow.value)
            return
        }
        service.getEmpresas().enqueue(object : Callback<List<EmpresaModel>> {
            override fun onResponse(
                call: Call<List<EmpresaModel>>,
                response: Response<List<EmpresaModel>>
            ) {
                if (response.isSuccessful) {
                    val empresas = response.body().orEmpty()
                    _empresasFlow.value = empresas
                    onSuccess(empresas)

                } else {
                    onError("No se pudieron cargar las empresas")
                }
            }

            override fun onFailure(call: Call<List<EmpresaModel>>, t: Throwable) {
                onError("Error al conectar con el servidor: ${t.localizedMessage}")
            }
        })
    }

    fun getBoletos(
        onSuccess: (List<Boleto>) -> Unit = {},
        onError: (String) -> Unit = { }
    ) {
        if (_boletosFlow.value.isNotEmpty()) {
            onSuccess(_boletosFlow.value)
            return
        }
        val empresa =
            sessionManager.getEmpresa()?.codigo ?: return onError("Empresa no configurada")

        val service = RetrofitClient.getApiService(empresa) ?: return onError("Error de red")

        service.getBoletos(sessionManager.getToken()).enqueue(object : Callback<List<Boleto>> {
            override fun onResponse(call: Call<List<Boleto>>, response: Response<List<Boleto>>) {
                Log.d("BOLETOS", "Response: ${response.code()}")
                if (response.code() == 200) {
                    val boletos =
                        response.body().orEmpty().sortedBy { it.orden }.filter { it.activo }
                    _boletosFlow.value = boletos
                    onSuccess(boletos)
                } else {
                    onError("No se pudieron cargar los boletos")
                }
            }

            override fun onFailure(call: Call<List<Boleto>>, t: Throwable) {
                onError("Error al conectar con el servidor:")
            }
        })
    }

    fun login(
        empresa: String,
        username: String,
        password: String,
        onSuccess: (LoginResponse) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val service = RetrofitClient.getApiService(empresa) ?: return onError("Error de red")
        val request = LoginRequest(username, password)

        service.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        sessionManager.saveLoginResponse(loginResponse)
                        onSuccess(loginResponse)
                    } else {
                        onError("Respuesta vacía del servidor")
                    }
                } else {
                    onError("Credenciales incorrectas")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError("Error al conectar con el servidor: ${t.localizedMessage}")
            }
        })
    }


    fun getRutas(
        onSuccess: (List<Ruta>) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (_rutasFlow.value.isNotEmpty()) {
            onSuccess(_rutasFlow.value)
            return
        }

        val empresa =
            sessionManager.getEmpresa()?.codigo ?: return onError("Empresa no configurada")

        val service = RetrofitClient.getApiService(empresa) ?: return onError("Error de red")

        service.getRutas(sessionManager.getToken()).enqueue(object : Callback<List<Ruta>> {
            override fun onResponse(call: Call<List<Ruta>>, response: Response<List<Ruta>>) {
                if (response.isSuccessful) {
                    val rutas = response.body().orEmpty()
                    _rutasFlow.value = rutas
                    onSuccess(rutas)
                } else {
                    onError("No se pudieron cargar las rutas")
                }
            }

            override fun onFailure(call: Call<List<Ruta>>, t: Throwable) {
                onError("Error al conectar con el servidor: ${t.localizedMessage}")
            }
        })
    }

    fun getParaderos(
        onSuccess: (List<Paradero>) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (_paraderosFlow.value.isNotEmpty()) {
            onSuccess(_paraderosFlow.value)
            return
        }

        val empresa =
            sessionManager.getEmpresa()?.codigo ?: return onError("Empresa no configurada")

        val service = RetrofitClient.getApiService(empresa) ?: return onError("Error de red")

        service.getParaderos(sessionManager.getToken()).enqueue(object : Callback<List<Paradero>> {
            override fun onResponse(
                call: Call<List<Paradero>>,
                response: Response<List<Paradero>>
            ) {
                if (response.isSuccessful) {
                    val paraderos = response.body().orEmpty()
                    _paraderosFlow.value = paraderos
                    Log.d("BOLETOS", "Paraderos: ${_paraderosFlow.value}")

                    onSuccess(paraderos)
                } else {
                    onError("No se pudieron cargar los paraderos")
                }
            }

            override fun onFailure(call: Call<List<Paradero>>, t: Throwable) {
                onError("Error al conectar con el servidor: ${t.localizedMessage}")
            }
        })
    }

    fun getTarifas(
        onSuccess: (List<Tarifa>) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (_tarifasFlow.value.isNotEmpty()) {
            onSuccess(_tarifasFlow.value)
            return
        }

        val empresa =
            sessionManager.getEmpresa()?.codigo ?: return onError("Empresa no configurada")

        val service = RetrofitClient.getApiService(empresa) ?: return onError("Error de red")

        service.geTarifas(sessionManager.getToken()).enqueue(object : Callback<List<Tarifa>> {
            override fun onResponse(call: Call<List<Tarifa>>, response: Response<List<Tarifa>>) {
                if (response.isSuccessful) {
                    val tarifas = response.body().orEmpty()
                    _tarifasFlow.value = tarifas
                    onSuccess(tarifas)
                } else {
                    onError("No se pudieron cargar las tarifas")
                }
            }

            override fun onFailure(call: Call<List<Tarifa>>, t: Throwable) {
                onError("Error al conectar con el servidor: ${t.localizedMessage}")
            }
        })
    }

    fun getRutaById(
        id: Int
    ): Ruta? {
        val ruta = _rutasFlow.value.find { it.id == id }
        if (ruta != null) {
            return ruta
        } else {
            return null
        }
    }

    fun getTarifaByBoletoId(
        id: Int
    ): Tarifa? {
        val tarifa = _tarifasFlow.value.find { it.id == id }
        if (tarifa != null) {
            return tarifa
        } else {
            return null
        }
    }

    fun getSiguienteParadero(
        paradero: Paradero
    ): Paradero? {
        val siguienteParadero = _paraderosFiltrados.value.find { it.orden == paradero.orden + 1 }
        return siguienteParadero
    }
}