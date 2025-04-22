package com.tcontur.login_tcontur.ui.core

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute(val route: String) {
    @Serializable
    object LoginRoute : AppRoute("login")

    @Serializable
    object HomeRoute : AppRoute("home")

    @Serializable
    object BoletoRoute : AppRoute("boleto")

    @Serializable
    data class Detail(val name: String) : AppRoute("detail")

    @Serializable
    object ReportRoute : AppRoute("report")

    @Serializable
    object ProtoRoute: AppRoute("proto")

    @Serializable
    object QrScanRoute: AppRoute("qrscanner")

    @Serializable
    object SplashRoute: AppRoute("splash")
}