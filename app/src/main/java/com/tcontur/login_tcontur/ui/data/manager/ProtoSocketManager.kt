package com.tcontur.login_tcontur.ui.core.protobin

import android.content.Context
import android.util.Log
import okhttp3.*
import okio.ByteString
import tcontur.com.Protocol
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class ProtoSocketManager(
    private val context: Context,
    private val schemaFileName: String = "schemas.json",
    private val debug: Boolean = false,
    private val onLoading: (Boolean) -> Unit = {},
    private val onMessageDecoded: (Map<String, Any>) -> Unit
) {
    private var client: OkHttpClient? = null
    private var socket: WebSocket? = null
    private var proto: Protocol? = null

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
        proto = Protocol(false, file.absolutePath, null)
    }

    fun connect(url: String) {
        onLoading(true)

        client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val request = Request.Builder().url(url).build()
        socket = client!!.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                log("Conexión abierta")
                onLoading(false)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                val rawBytes = bytes.toByteArray()
                if (debug) {
                    log(
                        "Bytes recibidos: ${
                            rawBytes.joinToString(" ") {
                                it.toUByte().toString(16)
                            }
                        }"
                    )
                }
                try {
                    val result = proto?.decode(rawBytes, null)
                    val data = result?.data
                    if (data is Map<*, *>) {
                        @Suppress("UNCHECKED_CAST")
                        onMessageDecoded(data as Map<String, Any>)
                    }
                } catch (e: Exception) {
                    log("Error de decodificación: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                onLoading(false)
                log("Error: ${t.message}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                log("Cerrando socket: $code / $reason")
                onLoading(false)
            }
        })
    }

    fun send(data: Map<String, String>, messageType: String) {
        val encoded = proto?.encode(data, messageType)
        socket?.send(ByteString.of(*encoded!!))
        log("Mensaje enviado: $data")
    }

    fun close() {
        socket?.close(1000, null)
        log("Socket cerrado")
    }

    private fun log(msg: String) {
        if (debug) Log.d("ProtoSocket", msg)
    }
}
