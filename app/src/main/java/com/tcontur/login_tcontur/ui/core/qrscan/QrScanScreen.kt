package com.tcontur.login_tcontur.ui.core.qrscan

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tcontur.login_tcontur.ui.core.AppRoute
import com.tcontur.login_tcontur.ui.data.manager.SessionManager
import com.tcontur.login_tcontur.ui.core.protobin.ProtoSocketManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScanScreen(
    navController: NavController,
    sessionManager: SessionManager,
    protoSocket: ProtoSocketManager,
) {
    val serial = 359769034416997
    var scan by remember { mutableStateOf(true) }
    var lastScan by remember { mutableStateOf("") } // Último escaneo

//    LaunchedEffect(Unit) {
//
//        sessionManager.protoLogin.collect { protoLog ->
//            protoLog?.let {
//                navController.navigate(AppRoute.HomeRoute) {
//                    popUpTo(AppRoute.QrScanRoute) {
//                        inclusive = true
//                    }
//                }
////                Log.d("QR_MLKIT", "QR Info: $it")
//            }
//        }
//    }


    fun procesarQrCode(rawCode: String) {
        val parsed = protoSocket.parseQrCode(rawCode)
        Log.d("QrScanScreen", "----------------------Aqui comenzamos el parsed $parsed")
        if (parsed != null) {
            sessionManager.updateQrInfo(parsed)
            scan = false
            val dataSerial: MutableMap<String, Any> = HashMap()
            dataSerial["serial"] = parsed.imei
            protoSocket.send(dataSerial, "login")
            navController.navigate(AppRoute.HomeRoute) {
                popUpTo(AppRoute.QrScanRoute) {
                    inclusive = true
                }
            }

            Log.d("QrScanScreen", "SE ENVIO EL SERIAL: $serial")
        } else {
            Log.e("QrScanScreen", "Código QR inválido")
        }

        parsed?.let {
            Log.d(
                "QrScanScreen",
                "Día: ${it.dia}, Sesión: ${it.sesion}, Ruta: ${it.ruta}, Lado: ${it.lado}, IMEI: ${it.imei}"
            )
        } ?: Log.e("QrScanScreen", "Código QR inválido")
    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Qr Scan") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Back"
//                        )
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {


//            if (!isConnected) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(text = "Conectando...")
////                    CircularProgressIndicator()
//                    LinearProgressIndicator(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(8.dp)
//                    )
//                }
//            } else {

        CodeScanner(
//                    modifier = Modifier.fillMaxSize(),
            onQrScanned = { rawCode ->
                if (lastScan != rawCode) {
                    procesarQrCode(rawCode)
                    lastScan = rawCode // Actualiza el último escaneo
                    Log.d("QrScanScreen", "Código QR escaneado: $rawCode")
                }
            },
            scanState = scan
        )
//            }

    }
//    }
}
