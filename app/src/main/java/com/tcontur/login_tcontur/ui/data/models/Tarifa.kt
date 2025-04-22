package com.tcontur.login_tcontur.ui.data.models

data class Tarifa(
    val id: Int,
    val boleto: Boleto,
    val nombre: String,
    val tarifa: Float,
    val color: String,
    val fin: Zona,
    val inicio: Zona,
)

data class Zona(
    val id: Int,
    val nombre: String,
)