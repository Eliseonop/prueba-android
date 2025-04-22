package com.tcontur.login_tcontur.ui.data.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ProtoLogin(
    val schedule: String?,
    val route: Int?,
    val trip: Int?,
    val driver: String?,
    val data: String?, // Puede ser String o LocalDateTime, se manejaría como String
    val geofence: String?,
    val logged: Boolean?,
    val padron: Int?,
    val id: Int?,
    val state: String?,
    val direction: Boolean?,
    val order: Int?
) {
    // Constructor que recibe un Map<String, Any> y asigna los valores automáticamente
    constructor(map: Map<String, Any>) : this(
        schedule = map["schedule"] as? String,
        route = map["route"] as? Int,
        trip = map["trip"] as? Int,
        driver = map["driver"] as? String,
        data = map["data"] as? String, // Puede adaptarse según el tipo de dato de "data"
        geofence = map["geofence"] as? String,
        logged = map["logged"] as? Boolean,
        padron = map["padron"] as? Int,
        id = map["id"] as? Int,
        state = map["state"] as? String,
        direction = map["direction"] as? Boolean,
        order = map["order"] as? Int
    )

    // Método para inicializar el objeto automáticamente a partir del Map
    companion object {
        fun fromMap(map: Map<String, Any>): ProtoLogin {
            // Aquí podemos utilizar reflexión para asignar los valores dinámicamente
            val schedule = map["schedule"] as? String
            val route = map["route"] as? Int
            val trip = map["trip"] as? Int
            val driver = map["driver"] as? String
//            val data =
//                map["data"] as? String // Aquí también podríamos convertir a LocalDateTime si es necesario
            // Si "data" es un LocalDateTime, puedes convertirlo aquí
             val data = (map["data"] as? LocalDateTime)?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                ?: map["data"] as? String // Si no es LocalDateTime, lo dejamos como String
            val geofence = map["geofence"] as? String
            val logged = map["logged"] as? Boolean
            val padron = map["padron"] as? Int
            val id = map["id"] as? Int
            val state = map["state"] as? String
            val direction = map["direction"] as? Boolean
            val order = map["order"] as? Int

            return ProtoLogin(
                schedule,
                route,
                trip,
                driver,
                data,
                geofence,
                logged,
                padron,
                id,
                state,
                direction,
                order
            )
        }
    }
}
//data class DecodeLogin(
//    val schedule: LocalTime,
//    val route: Int,
//    val trip: Int,
//    val driver: String?,
//    val data: String,
//    val geofence: String?,
//    val logged: Boolean,
//    val padron: Int,
//    val id: Int,
//    val state: String,
//    val direction: Boolean,
//    val order: Int
//) {
//    companion object {
//        fun fromMap(map: Map<String, Any>): DecodeLogin {
//            return DecodeLogin(
//                schedule = map["schedule"]?.let {
//                    when (it) {
//                        is LocalTime -> it // Si ya es LocalTime, lo dejamos tal cual
//                        is String -> LocalTime.parse(it, DateTimeFormatter.ISO_LOCAL_TIME) // Convertimos de String a LocalTime
//                        else -> throw IllegalArgumentException("Invalid type for schedule")
//                    }
//                } ?: throw IllegalArgumentException("Missing schedule"),
//
//                route = (map["route"] as? Number)?.toInt() ?: throw IllegalArgumentException("Invalid type for route"),
//                trip = (map["trip"] as? Number)?.toInt() ?: throw IllegalArgumentException("Invalid type for trip"),
//                driver = map["driver"] as? String,
//                data = map["data"] as? String ?: throw IllegalArgumentException("Invalid type for data"),
//                geofence = map["geofence"] as? String,
//                logged = map["logged"] as? Boolean ?: throw IllegalArgumentException("Invalid type for logged"),
//                padron = (map["padron"] as? Number)?.toInt() ?: throw IllegalArgumentException("Invalid type for padron"),
//                id = (map["id"] as? Number)?.toInt() ?: throw IllegalArgumentException("Invalid type for id"),
//                state = map["state"] as? String ?: throw IllegalArgumentException("Invalid type for state"),
//                direction = map["direction"] as? Boolean ?: throw IllegalArgumentException("Invalid type for direction"),
//                order = (map["order"] as? Number)?.toInt() ?: throw IllegalArgumentException("Invalid type for order")
//            )
//        }
//    }
//}