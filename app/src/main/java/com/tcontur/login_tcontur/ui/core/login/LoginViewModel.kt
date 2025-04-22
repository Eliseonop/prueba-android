package com.tcontur.login_tcontur.ui.core.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
//import com.tcontur.login_tcontur.ui.data.bd.entity.LoginEntity
//import com.tcontur.login_tcontur.ui.data.bd.repository.LoginRepository
import com.tcontur.login_tcontur.ui.data.manager.FetchManager
import com.tcontur.login_tcontur.ui.data.manager.SessionManager
import com.tcontur.login_tcontur.ui.data.models.EmpresaModel
import com.tcontur.login_tcontur.ui.data.models.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val fetchManager: FetchManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Estados UI
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _rememberMe = MutableStateFlow(false)
    val rememberMe: StateFlow<Boolean> = _rememberMe.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingEmpresas = MutableStateFlow(true)
    val isLoadingEmpresas: StateFlow<Boolean> = _isLoadingEmpresas.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _empresas = MutableStateFlow<List<EmpresaModel>>(emptyList())
    val empresas: StateFlow<List<EmpresaModel>> = _empresas.asStateFlow()

    private val _selectedEmpresa = MutableStateFlow<EmpresaModel?>(null)
    val selectedEmpresa: StateFlow<EmpresaModel?> = _selectedEmpresa.asStateFlow()




    // Estado de login guardado en Room
//    val loginUser = loginRepository.loginUser

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            fetchManager.clearFlows()
            sessionManager.clearSession()

            if (sessionManager.isRememberMe()) {
                _rememberMe.value = true
                _username.value = sessionManager.getUsername() ?: ""
            }

            fetchEmpresas()
        }
    }

    fun updateUsername(value: String) {
        _username.value = value
        _isError.value = false
    }

    fun updatePassword(value: String) {
        _password.value = value
        _isError.value = false
    }

    fun updateRememberMe(value: Boolean) {
        _rememberMe.value = value
    }

    fun updateSelectedEmpresa(empresa: EmpresaModel) {
        _selectedEmpresa.value = empresa
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun fetchEmpresas() {
        _isLoadingEmpresas.value = true
        fetchManager.getEmpresas(
            onSuccess = { empresasList ->
                _isLoadingEmpresas.value = false
                _empresas.value = empresasList

                if (sessionManager.isRememberMe()) {
                    val empresa = sessionManager.getEmpresa()
                    _selectedEmpresa.value = empresasList.find { it.codigo == empresa?.codigo }
                }
            },
            onError = { error ->
                _isLoadingEmpresas.value = false
                _errorMessage.value = error
                _showDialog.value = true
            }
        )
    }

    fun login(
        onSuccess: (LoginResponse) -> Unit = {},
    ) {
        if (_username.value.isBlank() || _password.value.isBlank()) {
            _errorMessage.value = "Usuario y contraseña son requeridos"
            _showDialog.value = true
            return
        }

        if (_selectedEmpresa.value == null) {
            _errorMessage.value = "Debe seleccionar una empresa"
            _showDialog.value = true
            return
        }

        _isLoading.value = true
        fetchManager.login(
            empresa = _selectedEmpresa.value?.codigo ?: "",
            username = _username.value,
            password = _password.value,
            onSuccess = { loginResponse ->
                _isLoading.value = false
                // Guardar en SharedPreferences
                sessionManager.saveLoginResponse(loginResponse)
                sessionManager.saveEmpresa(_selectedEmpresa.value)
                sessionManager.saveUsername(_username.value)
                sessionManager.saveRememberMe(_rememberMe.value)
                onSuccess(loginResponse)
            },
            onError = { error ->
                _isLoading.value = false
                _errorMessage.value = error
                _showDialog.value = true
            }
        )
    }

    fun logout() {
        viewModelScope.launch {
//            loginRepository.eliminarLogin()
            sessionManager.clearSession()
        }
    }
}

class LoginViewModelFactory(
    private val fetchManager: SessionManager,
    private val sessionManager: FetchManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel( sessionManager, fetchManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}