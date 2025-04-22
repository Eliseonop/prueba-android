package com.tcontur.login_tcontur.ui.core.protobin

import android.content.Context
import android.util.Log
import com.tcontur.login_tcontur.ui.data.models.QrData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import okio.ByteString
import tcontur.com.DecoderResult
import tcontur.com.Protocol
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class ProtoSocketManager(
    private val context: Context,
    private val schemaFileName: String = "schemas2.json",
    private val debug: Boolean = false,
    private val onMessageDecoded: (a: DecoderResult) -> Unit,
    private val onClose: (code: Int, reason: String) -> Unit,
//    private val onSuccess: (Boolean) -> Unit = {
//
//    },
) {
    private var client: OkHttpClient? = null
    private var socket: WebSocket? = null
    private var proto: Protocol? = null
    private val _isConected = MutableStateFlow<Boolean>(false)
    val isConected: StateFlow<Boolean> get() = _isConected

    init {
        prepareProto()
    }

    private fun prepareProto() {
        val file = File(context.filesDir, schemaFileName)
        if (!file.exists()) {
            context.assets.open(schemaFileName).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        try {
            proto = Protocol(false, file.absolutePath, null)
        } catch (e: Exception) {
            Log.e("ProtoSocketManager", "Error al cargar el esquema: ${e.message}")
        }
    }

    fun connect(
        url: String,
        onSuccess: (Boolean) -> Unit = {},
        onError: (String) -> Unit = {},
    ) {
//        onLoading(true)
        if (_isConected.value) {
            close() // o incluso hacer socket?.cancel() si querés forzar
        }
        client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()

        val request = Request.Builder().url(url).build()
        socket = client!!.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                log("Conexión abierta")
                _isConected.value = true
//                onLoading(false)
                onSuccess(true)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                val rawBytes = bytes.toByteArray()
                if (debug) {
                    log("Bytes recibidos: ${
                        rawBytes.joinToString(" ") {
                            it.toUByte().toString(16)
                        }
                    }")
                }
                try {
                    val result = proto?.decode(rawBytes, null)
                    if (debug) {
                        log("Mensaje recibido: $result")
                    }
                    if (result != null) {
                        onMessageDecoded(result) // <- Cambio aquí
                    }
                } catch (e: Exception) {
                    log("Error de decodificación: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _isConected.value = false
                log("Error Socket: ${t.message}")
                onError(t.message ?: "Error desconocido")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                _isConected.value = false
                webSocket.close(1000, null)
                onClose(code, reason)
                log("Cerrando socket: $code / $reason")
//                onLoading(false)
            }
        })
    }

    fun send(data: Map<String, Any?>, fortmatKey: String) {
        val encoded = proto?.encode(data, fortmatKey)
        socket?.send(ByteString.of(*encoded!!))
        log("Mensaje enviado: $data")
        log("Bytes enviados: ${
            encoded?.joinToString(" ") {
                it.toUByte().toString(16)
            }
        }")
    }

    fun close() {
        socket?.close(1000, null)
        log("Socket cerrado")
    }

    private fun log(msg: String) {
        if (debug) Log.d("ProtoSocket", msg)
    }

    fun parseQrCode(code: String): QrData? {
        val parts = code.split("|")
        return if (parts.size >= 5) {
            QrData(
                dia = parts[0],
                sesion = parts[1].toInt(),
                ruta = parts[2].toInt(),
                lado = parts[3] == "1",
                imei = parts[4]
            )
        } else {
            null // Código inválido o incompleto
        }
    }

//    fun clear() {
//        _isConected.value = false
////        socket?.close(1000, null)
//    }
}
