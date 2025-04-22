package com.tcontur.login_tcontur.ui.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "tickets")
data class TicketEntity(
    val fare: Int,
    val driver: Int,
    @PrimaryKey(autoGenerate = true) val correlative: Int = 0,
    val day: LocalDate,
    val busstopEnd: Int?,
    val time: LocalDateTime,
    val busstopStart: Int?,
    val inspector: Int?,
    val paymentMethod: Int,
    val numero: Int,
    val price: Float,
    val route: Int?,
    val session: Int,
    val vehicle: Int?,
    val card: String?
){
    fun toMap(): Map<String, Any?> = mapOf(
        "fare" to fare,
        "driver" to driver,
        "correlative" to correlative,
        "day" to day, // o conservar como LocalDate si vas a trabajar con Firestore y usar custom converters
        "busstop_end" to busstopEnd,
        "time" to time, // o .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        "busstop_start" to busstopStart,
        "inspector" to inspector,
        "payment_method" to paymentMethod,
        "number" to numero,
        "price" to price,
        "route" to route,
        "session" to session,
        "vehicle" to vehicle,
        "card" to card
    )
}
