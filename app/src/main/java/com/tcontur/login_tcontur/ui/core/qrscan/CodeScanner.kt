package com.tcontur.login_tcontur.ui.core.qrscan

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CodeScanner(
    modifier: Modifier = Modifier,
    onQrScanned: (String) -> Unit,
    scanState: Boolean = true,
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
//    val scanState by rememberUpdatedState(scan) // Actualizado en cada recomposición

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()


    }

    if (permissionState.status.isGranted) {
        val cameraProviderFuture = remember {
            ProcessCameraProvider.getInstance(context)
        }

        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().apply {
                        setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    val analysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(
                                Executors.newSingleThreadExecutor()
                            ) { imageProxy ->
                                if (scanState) {
                                    processImageProxy(imageProxy) { qrCode ->
                                        onQrScanned(qrCode)
                                    }
                                } else {
                                    imageProxy.close() // No procesa si se está procesando
                                }
                            }
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            analysis
                        )
                    } catch (e: Exception) {
                        Log.e("QrCodeScanner", "Error al enlazar cámara", e)
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = modifier.fillMaxSize()
        )
    } else {
        Text("Se necesita permiso de cámara para escanear el código QR.")
    }
}

@SuppressLint("UnsafeOptInUsageError")
private fun processImageProxy(
    imageProxy: ImageProxy,
    onQrDetected: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { value ->
//                        Log.d("QR_MLKIT", "Código escaneado: $value")
                        onQrDetected(value)

                    }
                }
            }
            .addOnFailureListener {
                Log.e("QrCodeScanner", "Error al escanear QR", it)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}