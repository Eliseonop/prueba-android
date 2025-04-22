package com.tcontur.login_tcontur.ui.data.models

import kotlinx.coroutines.flow.StateFlow

data class QrData(
    val dia: String,
    val sesion: Int,
    val ruta: Int,
    val lado: Boolean,
    val imei: String
)