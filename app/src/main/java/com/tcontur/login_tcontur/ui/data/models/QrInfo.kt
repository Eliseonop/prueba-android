package com.tcontur.login_tcontur.ui.data.models

data class QrInfo(
    val day: String,
    val route: Number,
    val session: Number,
    val vehicle: Number,
    val card: String?
) {

    constructor(
        day: String,
        route: Number,
        session: Number,
        vehicle: Number,
    ) : this(
        day = day,
        route = route,
        session = session,
        vehicle = vehicle,
        card = null
    )
}