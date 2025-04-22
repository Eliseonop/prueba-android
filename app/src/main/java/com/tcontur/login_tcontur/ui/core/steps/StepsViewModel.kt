package com.tcontur.login_tcontur.ui.core.steps

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class StepsViewModel : ViewModel() {
    private val _state = mutableStateOf(StepState())
    val state: State<StepState> = _state

    fun checkVersion() {
        _state.value = _state.value.copy(versionChecked = true)
    }

    fun grantLocationPermission() {
        _state.value = _state.value.copy(locationPermissionGranted = true)
    }

    fun obtainLocation() {
        _state.value = _state.value.copy(locationObtained = true)
    }
}
