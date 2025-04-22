package com.tcontur.login_tcontur.ui.core

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.tcontur.login_tcontur.ui.core.home.HomeScreen
import com.tcontur.login_tcontur.ui.core.login.LoginScreen
import com.tcontur.login_tcontur.ui.data.manager.SessionManager
import com.tcontur.login_tcontur.ui.core.protobin.ProtoScreen
import com.tcontur.login_tcontur.ui.core.protobin.ProtoSocketManager
import com.tcontur.login_tcontur.ui.core.qrscan.QrScanScreen
import com.tcontur.login_tcontur.ui.core.report.ReportScreen
import com.tcontur.login_tcontur.ui.core.venta.boletos.BoletosScreen
import com.tcontur.login_tcontur.ui.data.manager.FetchManager
import com.tcontur.login_tcontur.ui.data.models.ProtoLogin
import com.tcontur.login_tcontur.ui.utils.LocationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import tcontur.com.DecoderResult
import com.tcontur.login_tcontur.ui.data.models.Paradero
import com.tcontur.login_tcontur.ui.data.models.QrData


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavigationWrapper(context: Context) {
//    val db = AppDatabase.getDatabase(context)

    val navController = rememberNavController()
    val sessionManager = remember { SessionManager(context) }
    val fetchManager = remember { FetchManager(sessionManager) }
    val loginResponse = sessionManager.getLoginResponse()
    val paraderosFiltrados by fetchManager.paraderosFiltrados.collectAsState(emptyList())

    val protoSocket = ProtoSocketManager(
        context = context,
        debug = true,
        onMessageDecoded = { result: DecoderResult ->
            Log.d("BOLETOS", "Mensaje result desde navigation: $result")
            if (result.header == "login") {
                val decoded = ProtoLogin.fromMap(result.data as Map<String, Any>)
                sessionManager.updateLogin(decoded)
                Log.d("BOLETOS", "Login decoded: $decoded")
            }
        },
        onClose = { code, reason ->

        },
    )

    val startDestination = if (loginResponse != null) AppRoute.SplashRoute else AppRoute.LoginRoute

    fun procesarParaderosFiltrados() {
        if (paraderosFiltrados.isNotEmpty()) {
            LocationUtils.obtenerUbicacionActual(context) { location ->
                location?.let {
                    val paraderoActual = LocationUtils.encontrarParaderoMasCercano(
                        it.latitude, it.longitude, paraderosFiltrados
                    )

                    val paraderoSiguiente = paraderosFiltrados.find {
                        it.orden == (paraderoActual?.orden?.plus(1))
                    }
                    Log.d(
                        "BOLETOS", "Latitud: ${it.latitude}, Longitud: ${it.longitude}"
                    )

                    Log.d("BOLETOS", "Paradero Actual: $paraderoActual")
                    Log.d("BOLETOS", "Paradero Siguiente: $paraderoSiguiente")

                    if (paraderoActual != null) {
                        sessionManager.updateActualSiguiente(
                            paraderoActual
                        )
                    }
                }
            }
        }
    }
    LaunchedEffect(sessionManager.qrInfo, fetchManager.paraderosFlow) {
        Log.d("BOLETOS", "Iniciando la recolección de paraderos filtrados")
        combine(
            sessionManager.qrInfo,
            fetchManager.paraderosFlow
        ) { qrData, paraderosList ->
            if (qrData != null && paraderosList.isNotEmpty()) {
                Log.d("BOLETOS", "QR Data: $qrData")
                Log.d("BOLETOS", "Paraderos List: $paraderosList")
                filtrarParaderosPorRuta(paraderosList, qrData)
            } else {
                emptyList()
            }
        }.collect { filtrados ->
//                paraderosFiltrados = filtrados
            fetchManager.updateParaderosFiltrados(filtrados)
            Log.d("BOLETOS", "Paraderos actualizados: $filtrados")
            procesarParaderosFiltrados() // se ejecuta justo cuando cambia algo
        }
    }
    LaunchedEffect(paraderosFiltrados) {
        while (loginResponse != null) {
            procesarParaderosFiltrados()
            delay(60_000) // esperar 1 minuto
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable<AppRoute.LoginRoute> {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(AppRoute.SplashRoute) {
                        popUpTo(AppRoute.LoginRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                sessionManager = sessionManager,
                fetchManager = fetchManager,
            )
        }
        composable<AppRoute.HomeRoute> {
            HomeScreen(
                navController = navController,
                sessionManager = sessionManager,
                protoSocket = protoSocket,
                fetchManager = fetchManager
            )
        }
//        composable<Detail> { backStackEntry ->
//            val detail = backStackEntry.toRoute<Detail>()
//            DetailScreen(detail.name)
//        }

        composable<AppRoute.BoletoRoute> {
            BoletosScreen(
                navController = navController,
                sessionManager = sessionManager,
                fetchManager = fetchManager,
                protoSocket = protoSocket
            )

        }

        composable<AppRoute.ReportRoute> {
            ReportScreen(navController = navController)
        }

        composable<AppRoute.ProtoRoute> {
            ProtoScreen(navController = navController)
        }

        composable<AppRoute.QrScanRoute> {
            QrScanScreen(
                navController = navController,
                protoSocket = protoSocket,
                sessionManager = sessionManager,
            )
        }

        composable<AppRoute.SplashRoute> {
            SplashScreen(context = context,
                sessionManager = sessionManager,
                fetchManager = fetchManager,
                navController = navController,
                onReady = {
                    navController.navigate(AppRoute.HomeRoute) {
                        popUpTo(AppRoute.SplashRoute) { inclusive = true }
                    }
                })
        }
    }
}

fun filtrarParaderosPorRuta(paraderos: List<Paradero>, qrData: QrData?): List<Paradero> {
    if (qrData == null) return emptyList()

    return paraderos.filter { paradero ->
        paradero.ruta.id == qrData.ruta && paradero.lado == qrData.lado
    }.filter { paradero -> paradero.activo }.sortedBy { p ->
        p.orden
    }
}