package com.tcontur.login_tcontur.ui.data.viewmodel.boletos

data class Boleto(
    val id: Int,
    val orden: Int,
    val nombre: String,
    val serie: String,
    val tarifa: Float,
    val color: String,
    val activo: Boolean,
)