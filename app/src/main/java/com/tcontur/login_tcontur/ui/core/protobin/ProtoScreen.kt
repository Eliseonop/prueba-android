package com.tcontur.login_tcontur.ui.core.protobin
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import tcontur.com.Protocol
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

fun copiarAssetAArchivo(context: Context, nombreAsset: String, destino: File) {
    context.assets.open(nombreAsset).use { input ->
        FileOutputStream(destino).use { output ->
            input.copyTo(output)
        }
    }
}

@Composable
fun ProtoScreen(navController: NavController) {
    val context = LocalContext.current
    val archivoJson = File(context.filesDir, "schemas.json")
    if (!archivoJson.exists()) {
        copiarAssetAArchivo(context, "schemas.json", archivoJson)
    }
    var client: Protocol? = null
    var server: Protocol? = null
    try {
        client = Protocol(false, archivoJson.absolutePath, null)
        server = Protocol(true, archivoJson.absolutePath, null)

        val data = byteArrayOf(
            0x54.toByte(),
            0x3d.toByte(),
            0x00.toByte(),
            0x01.toByte(),
            0x00.toByte(),
            0x06.toByte(),
            0x03.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x54.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x0c.toByte(),
            0x15.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x01.toByte(),
            0x01.toByte(),
            0x17.toByte(),
            0x03.toByte(),
            0x25.toByte(),
            0x16.toByte(),
            0x54.toByte(),
            0x45.toByte(),
            0x05.toByte(),
            0x26.toByte()
        )

        val decodedResult = client.decode(data, "login")
        Log.e("Decoded", decodedResult.data.toString())

    } catch (e: Exception) {
        Log.e("Error", e.toString())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
    ) {
        DecodingSection(client, server)
//        Divider(modifier = Modifier.padding(vertical = 8.dp))
        SocketSection(cproto = client)
    }
}

@Composable
fun DecodingSection(
    client: Protocol?,
    server: Protocol?
) {
    if (client == null || server == null) {
        return
    }
    var decodedResultState by remember { mutableStateOf<Any?>(null) }
    var headerState by remember { mutableStateOf("") }
    var decodeDuration by remember { mutableStateOf<Long?>(null) }
    val data: Map<String, String> = mapOf("serial" to "15984316545")
    val encodedData: ByteArray = client.encode(data, "login")

    // Interfaz para probar la decodificación
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        decodeDuration?.let { duration ->
            Text(text = "Tiempo de decodificación: $duration ms")
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (headerState.isNotEmpty()) {
            Text(text = "Header decodificado: $headerState")
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (decodedResultState is Map<*, *>) {
            Text(text = "Datos decodificados:")
            Spacer(modifier = Modifier.height(8.dp))
            (decodedResultState as Map<*, *>).forEach { (key, value) ->
                Text(text = "$key: $value")
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Button(
                onClick = {
                    decodeDuration = 0
                    decodedResultState = ""
                    headerState = ""
                }
            ) {
                Text("Limpiar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val startTime = System.currentTimeMillis()
                    val decodedResult = server.decode(encodedData, null)
                    val endTime = System.currentTimeMillis()

                    decodeDuration = endTime - startTime
                    decodedResultState = decodedResult.data
                    headerState = decodedResult.header ?: ""
                }
            ) {
                Text("Decodificar y mostrar PRUEBA")
            }
        }
    }
}

@Composable
fun SocketSection(
    cproto: Protocol?
) {
    if (cproto == null) {
        return
    }
    val messages = remember { mutableStateListOf<String>() }
    var webSocketState by remember { mutableStateOf<WebSocket?>(null) }
    var inputMessage by remember { mutableStateOf("report") }
    val context = LocalContext.current

    val data: Map<String, String> = mapOf("serial" to "359769034416997")

    LaunchedEffect(Unit) {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
        val request = Request.Builder()
            .url("ws://34.27.2.64:22222?tipo=D&version=1&ticketera=true")
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                messages.add("Conexión abierta")
                webSocketState = webSocket
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                messages.add("Recibido: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                messages.add("Recibido $inputMessage (bytes): ${
                    bytes.toByteArray().joinToString(separator = " ") {
                        it.toString(16)
                    }
                }")
                Log.e("Bytes", bytes.toByteArray().joinToString(separator = " ") {
                    it.toString(16)
                })
                try {
                    val decodedResult = cproto.decode(bytes.toByteArray(), null)
                    val datas = decodedResult.data
                    messages.add("Decodificado : $datas")

                } catch (e: Exception) {
                    Log.e("Error", e.toString())
                    messages.add("Error: ${e.localizedMessage}")
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                messages.add("Cerrando: $code / $reason")
                webSocket.close(1000, null)
                webSocketState = null
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                messages.add("Error: ${t.localizedMessage}")
                webSocketState = null
            }
        }
        client.newWebSocket(request, listener)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Lista de mensajes del WebSocket
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { msg ->
                Text(text = msg, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = inputMessage,
            onValueChange = { inputMessage = it },
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                var data: Map<String, Any?>? = null
                if (inputMessage == "login") {
                    data = mapOf("serial" to "359769034416997")
                } else if (inputMessage == "report") {
                    val dataPos = mapOf(
                        "positions" to listOf(
                            mapOf(
                                "time" to LocalDateTime.of(2024, 10, 18, 15, 57, 38),
                                "lng" to -77.015533,
                                "lat" to -12.061365,
                                "speed" to 105,
                                "mark" to null,
                                "busstop" to null
                            )
                        ),
                        "sales" to 0,
                        "events" to emptyList<Any>(),
                        "direction" to null,
                        "route" to null,
                        "state" to null,
                        "boletos" to listOf(
                            mapOf(
                                "id" to 1,
                                "cantidad" to 1,
                            )
                        ),
                        "trip" to null
                    )
                    data = dataPos
                }

                messages.add("Enviando: $inputMessage")
                try {
                    Log.e("Data", data.toString())
                    messages.add("Encode data...")
                    val binaryData: ByteArray = cproto.encode(data, inputMessage)
                    webSocketState?.send(ByteString.of(*binaryData))?.also {
                        messages.add(
                            "Enviado binario: ${
                                binaryData.joinToString(separator = " ") {
                                    it.toString(
                                        16
                                    )
                                }
                            }"
                        )
                    } ?: messages.add("WebSocket no conectado")

                } catch (e: Exception) {
                    Log.e("Error", e.toString())
                    messages.add("Error: ${e.localizedMessage}")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Data ")
        }
    }
}

@Preview
@Composable
fun ProtoScreenPreview() {
    ProtoScreen(
        navController = NavController(
            context = LocalContext.current
        )
    )
}