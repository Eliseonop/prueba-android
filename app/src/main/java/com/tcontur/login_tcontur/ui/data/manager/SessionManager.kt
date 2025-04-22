package com.tcontur.login_tcontur.ui.data.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.tcontur.login_tcontur.ui.data.db.dao.BoletoCountDao
import com.tcontur.login_tcontur.ui.data.db.entity.BoletoCountEntity
import com.tcontur.login_tcontur.ui.data.models.Boleto
import com.tcontur.login_tcontur.ui.data.models.EmpresaModel
import com.tcontur.login_tcontur.ui.data.models.LoginResponse
import com.tcontur.login_tcontur.ui.data.models.Paradero
import com.tcontur.login_tcontur.ui.data.models.ProtoLogin
import com.tcontur.login_tcontur.ui.data.models.QrData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


private const val KEY_LOGIN_RESPONSE = "login_response"
private const val KEY_EMPRESA = "empresa_model"
private const val KEY_REMEMBER_ME = "remember_me"
private const val KEY_USERNAME = "username"
private const val KEY_QR_INFO = "qr_info"

class SessionManager(
    context: Context,
) {

//    val loginDao = db.loginDao()

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _protoLogin = MutableStateFlow<ProtoLogin?>(null)
    val protoLogin: StateFlow<ProtoLogin?> get() = _protoLogin

    private val _userLogin = MutableStateFlow<LoginResponse?>(null)
    val userLogin: StateFlow<LoginResponse?> get() = _userLogin


    private val _qrInfo = MutableStateFlow<QrData?>(null)
    val qrInfo: StateFlow<QrData?> get() = _qrInfo

    private val _actual = MutableStateFlow<Paradero?>(null)
    val actual: StateFlow<Paradero?> get() = _actual

    private val _counters = MutableStateFlow<List<BoletoCountEntity>>(emptyList())
    val counters: StateFlow<List<BoletoCountEntity>> = _counters.asStateFlow()

    suspend fun loadAllCounters(
        boletoCountDao: BoletoCountDao
    ) {
        try {
            val allCounters = boletoCountDao.getAllCounters()
            _counters.value = allCounters
        } catch (e: Exception) {
            // Manejo de errores
            _counters.value = emptyList()
        }
    }

    suspend fun updateCounter(boleto: Boleto,lastNumero: Int,  boletoCountDao: BoletoCountDao) {
        boletoCountDao.upsertCounter(
            BoletoCountEntity(
                fare = boleto.id,
                lastNumero = lastNumero
            )
        )

//        boletoCountDao.upsertCounter(counter)
        loadAllCounters(boletoCountDao)
    }

//    private val _boletosVendidos = MutableStateFlow<Map<Int, Int>>(emptyMap())
//    val boletosVendidos = _boletosVendidos.asStateFlow()


//    fun registrarBoletoVendido(boletoId: Int, cantidad: Int) {
//        val currentMap = _boletosVendidos.value.toMutableMap()
//        val currentCount = currentMap[boletoId] ?: 0
//        currentMap[boletoId] = currentCount + cantidad
//        _boletosVendidos.value = currentMap
//
//        // Optionally save to local storage
//        saveBoletosVendidosToStorage(currentMap)
//    }

//    private fun saveBoletosVendidosToStorage(vendidos: Map<Int, Int>) {
//        val json = gson.toJson(vendidos)
//        prefs.edit().putString("boletos_vendidos", json).apply()
//    }

//    private fun loadBoletosVendidosFromStorage() {
//        val json = prefs.getString("boletos_vendidos", null)
//        if (json != null) {
//            val mapType = object : com.google.gson.reflect.TypeToken<Map<Int, Int>>() {}.type
//            val loadedMap: Map<Int, Int> = gson.fromJson(json, mapType)
//            _boletosVendidos.value = loadedMap
//        }
//    }


    fun updateActualSiguiente(paraderos: Paradero) {
        _actual.value = paraderos
    }


    init {
        Log.d("SessionManager", "SessionManager initialized")
        loadLoginResponse()

    }

    fun updateQrInfo(qrData: QrData) {
        Log.d("SessionManager", "QR Data saved: $qrData")
        val json = gson.toJson(qrData)
        prefs.edit().putString(KEY_QR_INFO, json).apply()
        _qrInfo.value = qrData
    }

    fun loadQrInfo(): QrData? {
        val json = prefs.getString(KEY_QR_INFO, null)
        _qrInfo.value = json?.let {
            gson.fromJson(it, QrData::class.java)
        }
        Log.d("SessionManager", "QR Data loaded: ${_qrInfo.value}")
        return _qrInfo.value
    }

    fun getDecodedLogin(): ProtoLogin? {
        return _protoLogin.value
    }

    private fun loadLoginResponse() {
        val json = prefs.getString(KEY_LOGIN_RESPONSE, null)
        _userLogin.value = json?.let {
            gson.fromJson(it, LoginResponse::class.java)
        }
        Log.d("SessionManager", "LoginResponse loaded: ${_userLogin.value}")
    }


    // Guardar el objeto LoginResponse completo
    fun saveLoginResponse(loginResponse: LoginResponse) {
        val json = gson.toJson(loginResponse)
        prefs.edit().putString(KEY_LOGIN_RESPONSE, json).apply()
        _userLogin.value = loginResponse
    }

    fun saveUsername(username: String) {
        prefs.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun getLoginResponse(): LoginResponse? = _userLogin.value

    // Guardar el estado de "Recordarme"
    fun saveRememberMe(rememberMe: Boolean) {
        prefs.edit().putBoolean(KEY_REMEMBER_ME, rememberMe).apply()
    }

    fun getToken(): String = "Token " + _userLogin.value?.token

    fun isRememberMe(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)

    // Guardar y recuperar empresa
    fun saveEmpresa(empresa: EmpresaModel?) {
        val json = gson.toJson(empresa)
        prefs.edit().putString(KEY_EMPRESA, json).apply()
    }

    fun getEmpresa(): EmpresaModel? {
        val json = prefs.getString(KEY_EMPRESA, null)
        return if (json != null) gson.fromJson(json, EmpresaModel::class.java) else null
    }

    // Limpiar toda la sesión (login + empresa)
    fun clearSession() {
        prefs.edit()
            .remove(KEY_LOGIN_RESPONSE)
            .apply()
        _userLogin.value = null
        _protoLogin.value = null
        _qrInfo.value = null
        _actual.value = null
    }

    // Setters rápidos para otras variables internas
    fun updateLogin(decoded: ProtoLogin) {
        _protoLogin.value = decoded
    }

//    fun updateQrInfo(qrInfo: QrInfo) {
//        _qrInfo.value = qrInfo
//    }
}