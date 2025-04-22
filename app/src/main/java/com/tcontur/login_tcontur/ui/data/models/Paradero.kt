package com.tcontur.login_tcontur.ui.data.models

data class Paradero(
    val id: Int,
    val lado: Boolean,
    val ruta: Ruta,
    val activo: Boolean,
    val nombre: String,
    val orden: Int,
    // puede ser  -12.1231231231321
    val latitud: Double,
    // puede ser  -77.1231231231321
    val longitud: Double,
)