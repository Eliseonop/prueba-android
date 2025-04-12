package com.tcontur.login_tcontur.ui.core.venta

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ftpos.library.smartpos.printer.PrintStatus

@Composable
fun PrinterStatusScreen() {
    var printerStatus by remember { mutableStateOf(PrintStatus()) }
    var context = LocalContext.current

//    val deviceManager = remember { DeviceManager() }
    // Actualización periódica del estado de la impresora
//

    LaunchedEffect(Unit) {
//        deviceManager.initializeDevices(context)
//        PrinterManager.init(context)

    }

    // Llamar al método de permisos

    // Aquí tu interfaz de usuario
//    Text("Hola, mundo!")


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = {
//            PrinterManager.imprimir("Hola, mundo!", onComplete = {
////                printerStatus = PrinterManager.getPrinter()?.getStatus(printerStatus)
////                printerStatus = "Impresión completada"
//            }, onError = { code, error ->
////                printerStatus = PrinterManager.getPrinterStatus()
//            })

        }) {
            Text("Check print")
        }

        Text(
            text = "Estado de la Impresora",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        PrinterStatusView(printerStatus)
    }
}

@Composable
fun PrinterStatusView(status: PrintStatus) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Estado del papel
            StatusItem(
                icon = if (status.getmIsHavePaper() == true) Icons.Filled.CheckCircle else Icons.Filled.Error,
                iconTint = if (status.getmIsHavePaper() == true) Color.Green else Color.Red,
                label = "Papel",
                value = if (status.getmIsHavePaper() == true) "Disponible" else "Sin papel"
            )

            // Estado de la batería
            StatusItem(
                icon = if (status.getmIsLowBattery() == 0) Icons.Filled.Battery5Bar else Icons.Filled.Battery0Bar,
                iconTint = if (status.getmIsLowBattery() == 0) Color.Green else Color.Red,
                label = "Batería",
                value = if (status.getmIsLowBattery() == 0) "Normal" else "Baja"
            )

            // Temperatura
            StatusItem(
                icon = Icons.Filled.Thermostat,
                iconTint = when {
                    status.getmTemperature() > 40 -> Color.Red
                    status.getmTemperature() > 35 -> Color.Yellow
                    else -> Color.Green
                },
                label = "Temperatura",
                value = "${status.getmTemperature()}°C"
            )

            // Nivel de gris
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Print,
                        contentDescription = "Nivel de gris",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Nivel de gris: ${status.getmGray()}/10")
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { status.getmGray() / 10f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            // Mostrar la representación string de PrintStatus
            Text(
                text = "Información completa: ${status.toString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun StatusItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint
        )

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}