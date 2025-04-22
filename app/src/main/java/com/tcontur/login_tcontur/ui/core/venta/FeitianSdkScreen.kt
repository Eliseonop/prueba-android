package com.tcontur.login_tcontur.ui.core.venta

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ftpos.library.smartpos.buzzer.Buzzer
import com.ftpos.library.smartpos.buzzer.BuzzerMode
import com.ftpos.library.smartpos.errcode.ErrCode
import com.tcontur.login_tcontur.ui.data.manager.PrinterManager
import com.tcontur.login_tcontur.ui.data.manager.DeviceManager

@Composable
fun FeitianSdkScreen() {
    val context = LocalContext.current
    var sdkVersion by remember { mutableStateOf("Presiona el botón") }
    var mensaje by remember { mutableStateOf("") }
    val printer = PrinterManager(DeviceManager.printer)
    // ⚠️ Inicializa el PrinterManager
    LaunchedEffect(Unit) {
//        PrinterManager.init(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Versión del SDK: $sdkVersion", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { sdkVersion = getSDKVersion(context) }) {
            Text(text = "Obtener Versión del SDK")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val textToPrint = "Versión del SDK:\n$sdkVersion"
            printer.imprimir(
                text = textToPrint,
                onComplete = {
                    mensaje = "✅ Impresión completada"
                },
                onError = { code, error ->
                    mensaje = "❌ Error al imprimir: $code - $error"
                }
            )
        }) {
            Text("Imprimir versión SDK")
        }
        BuzzerControlPanel(buzzer = DeviceManager.buzzer)
//        Button(onClick = {
//            val buzzer = DeviceRegistry.buzzer
//            buzzer.beep(1000, 1000, 1, 1)
//        }) {
//            Text("Sonido")
//        }

        if (mensaje.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = mensaje, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun getSDKVersion(context: Context): String {
    return try {
        val deviceInstance = DeviceManager.device
        val sdkVersion = deviceInstance.sdkVersionName
        return sdkVersion ?: "SDK no disponible"
    } catch (e: Exception) {
        Log.e("FeitianSDK", "Error al obtener versión del SDK", e)
        "Error al obtener SDK"
    }
}


@Composable
fun BuzzerControlPanel(buzzer: Buzzer) {
    var frequency by remember { mutableFloatStateOf(2000f) } // Frecuencia inicial
    var volume by remember { mutableFloatStateOf(4f) } // Volumen inicial
    var times by remember { mutableFloatStateOf(3F) } // Veces de beep
    var onTimes by remember { mutableFloatStateOf(200f) } // Tiempo de encendido
    var offTimes by remember { mutableFloatStateOf(150f) } // Tiempo de apagado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Control del Buzzer", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Slider para Frecuencia
        Text("Frecuencia: ${frequency.toInt()} Hz")
        Slider(
            value = frequency,
            onValueChange = { frequency = it },
            valueRange = 500f..6000f,
            steps = 5500
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Slider para Volumen
        Text("Volumen: ${volume.toInt()}")
        Slider(
            value = volume,
            onValueChange = { volume = it },
            valueRange = 0f..10f,
            steps = 10
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Slider para Número de Beeps
        Text("Número de Beeps: $times")
        Slider(
            value = times.toFloat(),
            onValueChange = { times = it.toInt().toFloat() },
            valueRange = 1f..10f,
            steps = 9
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Slider para Tiempo Encendido
        Text("Tiempo Encendido: ${onTimes.toInt()} ms")
        Slider(
            value = onTimes,
            onValueChange = { onTimes = it },
            valueRange = 50f..1000f,
            steps = 950
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Slider para Tiempo Apagado
        Text("Tiempo Apagado: ${offTimes.toInt()} ms")
        Slider(
            value = offTimes,
            onValueChange = { offTimes = it },
            valueRange = 50f..1000f,
            steps = 950
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para activar el buzzer
        Button(onClick = { buzzerBeep(buzzer, times, onTimes, offTimes) }) {
            Text("Activar Buzzer")
        }
    }
}

fun buzzerBeep(buzzer: Buzzer, times: Float, onTimes: Float, offTimes: Float) {
    // Invocar el buzzer con los parámetros seleccionados
    val ret = buzzer.beep(times.toInt(), onTimes.toInt(), offTimes.toInt(), BuzzerMode.BUZZER_MODE_SYNC)
    if (ret == ErrCode.ERR_SUCCESS) {
        Log.d("Buzzer", "Buzzer activado correctamente")
        // Éxito
//        Toast.makeText(MainActivity.getContext(), "Buzzer activado correctamente", Toast.LENGTH_SHORT).show()
    } else {
        Log.e("Buzzer", "Error al activar el Buzzer: 0x${ret.toString(16)}")
        // Error
//        Toast.makeText(MainActivity.getContext(), "Error al activar el Buzzer: 0x${ret.toString(16)}", Toast.LENGTH_SHORT).show()
    }
}