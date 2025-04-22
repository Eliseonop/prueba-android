package com.tcontur.login_tcontur.ui.core.venta.boletos

import java.time.LocalDate
import java.time.LocalDateTime

data class TicketEncode(
    val fare: Int,
    val driver: Int,
    val correlative: Int? = null,
    val day: LocalDate,
    val busstopEnd: Int?,
    val time: LocalDateTime,
    val busstopStart: Int?,
    val inspector: Int? = null,
    val paymentMethod: PaymentMethod,
    val numero: Int,
    val price: Float,
    val route: Int?,
    val session: Int,
    val vehicle: Int?,
    val card: String? = null
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "fare" to fare,
        "driver" to driver,
        "correlative" to correlative,
        "day" to day,         // opcionalmente: conservar como LocalDate
        "busstop_end" to busstopEnd,
        "time" to time,       // opcionalmente: conservar como LocalTime
        "busstop_start" to busstopStart,
        "inspector" to inspector,
        "payment_method" to paymentMethod.code,
        "number" to numero,
        "price" to price,
        "route" to route,
        "session" to session,
        "vehicle" to vehicle,
        "card" to card
    )
}

enum class PaymentMethod(val code: Int) {
    EFECTIVO(0),
    QR(1),
    VISA(2),
    VALIDADOR(3)
}