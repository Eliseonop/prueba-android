package com.tcontur.login_tcontur.ui.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boleto_counters")
data class BoletoCountEntity(
    @PrimaryKey val fare: Int,
    val lastNumero: Int
)
