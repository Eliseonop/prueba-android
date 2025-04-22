package com.tcontur.login_tcontur.ui.core.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Aod
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.tcontur.login_tcontur.ui.core.AppRoute
import com.tcontur.login_tcontur.ui.data.manager.SessionManager
import com.tcontur.login_tcontur.ui.core.protobin.ProtoSocketManager
import com.tcontur.login_tcontur.ui.core.venta.NavigateCard
import com.tcontur.login_tcontur.ui.core.venta.boletos.BusRouteComponent
import com.tcontur.login_tcontur.ui.data.manager.FetchManager
import com.tcontur.login_tcontur.ui.data.models.Paradero
import com.tcontur.login_tcontur.ui.data.models.ProtoLogin

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    sessionManager: SessionManager,
    protoSocket: ProtoSocketManager,
    fetchManager: FetchManager
) {
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    var menuExpanded by remember { mutableStateOf(false) }
    val decodedLogin by sessionManager.protoLogin.collectAsState()
    val userLogin by sessionManager.userLogin.collectAsState()
    val socketConected by protoSocket.isConected.collectAsState()
    val actual by sessionManager.actual.collectAsState()
    var isConnected by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }
    LaunchedEffect(Unit) {
        protoSocket.connect(
            "ws://35.184.145.81:22222?version=2",
            onSuccess = {
                isConnected = true
                Log.d("QrScanScreen", "Conectado")

                val qrInfo = sessionManager.loadQrInfo()
                if (qrInfo != null) {
                    val dataSerial: MutableMap<String, Any> = HashMap()
                    dataSerial["serial"] = qrInfo.imei
                    protoSocket.send(dataSerial, "login")
                    Log.d("QrScanScreen", "Enviando información de QR: $qrInfo")
                } else {
                    Log.d("QrScanScreen", "No hay información de QR para enviar")
                }

            },
            onError = { reason ->
                Log.d("QrScanScreen", "Error de conexión: $reason")
            },
        )
    }


    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier
                .padding(0.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF8db8d9), Color(0xFF0066a8))
                    )
                ),
            title = {
                Text(
                    "${userLogin?.empresa?.uppercase() + " - "}${userLogin?.nombre}",
                    fontSize = 18.sp,
                )
            },
            actions = {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú")
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(text = { Text("Salir") }, onClick = {
                        menuExpanded = false
                        // Navega a la pantalla que desees (por ejemplo, Login)

                        navController.navigate(AppRoute.LoginRoute) {
                            popUpTo(AppRoute.LoginRoute) { inclusive = true }
                            launchSingleTop = true
                        }
                    })
                }
            },
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF8db8d9), Color(0xFF0066a8))
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // o Arrangement.spacedBy(16.dp)
            ) {

                QrLoad(
                    sessionManager = sessionManager,
                    qrButtonEnabled = socketConected,
                    onQrClick = {
                        navController.navigate(AppRoute.QrScanRoute)
                    }
                )
                BusRouteComponent(
                    inicio = actual?.nombre ?: "No disponible",
                    final = actual?.let { fetchManager.getSiguienteParadero(it) }?.nombre
                        ?: "No disponible",
                    loading = actual == null
                )

                Spacer(modifier = Modifier.height(16.dp))

                ToolsScreen(
                    navController,
                    decodedLogin,
                    paraderos = actual
                )

            }
        }

    }

}

@Composable
fun ToolsScreen(
    navController: NavController, decodedLogin: ProtoLogin? = null,
    paraderos: Paradero?
) {
    // Lista de items. Puedes agregar más o cambiar colores e íconos.
    val toolItems = listOf(
        NavigateCard(
            name = "Boletos",
            description = "Accede al módulo de boletos",
            icon = Icons.Filled.Aod,   // Ejemplo de ícono
            color = Color(0xFFFFA726),         // Ejemplo de color naranja
            route = AppRoute.BoletoRoute,
            enabled = decodedLogin != null && paraderos != null
        ),
//        NavigateCard(
//            name = "Reportes",
//            description = "Módulo de reportes",
//            icon = Icons.Filled.Assessment,     // Ejemplo de ícono
//            color = Color(0xFF66BB6A),         // Ejemplo de color verde
//            route = AppRoute.ReportRoute,
//            enabled = true
//        )
    )
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(toolItems) { item ->
                ToolCard(
                    item, onClick = {
                        navController.navigate(item.route)
                    }, enabled = item.enabled
                )
            }
        }
    }
}

@Composable
fun ToolCard(
    toolItem: NavigateCard,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val cardModifier = if (enabled) {
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    } else {
        Modifier.fillMaxWidth()
    }

    val cardColor =
        if (enabled) toolItem.color else Color.LightGray // puedes ajustar el color deshabilitado

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color.Blue, containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = toolItem.icon,
                contentDescription = toolItem.name,
                tint = Color.White.copy(alpha = if (enabled) 1f else 0.5f),
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = toolItem.name, style = TextStyle(
                        color = Color.White.copy(alpha = if (enabled) 1f else 0.5f),
                        fontSize = 30.sp,
                    )
                )
                Text(
                    text = toolItem.description, style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = if (enabled) 1f else 0.5f)
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun ToolCardPreview() {

}