package com.tcontur.login_tcontur.ui.core

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.tcontur.login_tcontur.R
import com.tcontur.login_tcontur.ui.data.manager.FetchManager
import com.tcontur.login_tcontur.ui.data.manager.SessionManager

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(
    navController: NavController,
    context: Context,
    sessionManager: SessionManager,
    fetchManager: FetchManager,
    onReady: () -> Unit
) {
    // Total de cargas a realizar (boletos, rutas, tarifas, paraderos)
    val totalLoads = 4
    var completedLoads by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    // Calcula el progreso actual (de 0.0f a 1.0f)
    val progress by remember { derivedStateOf { completedLoads.toFloat() / totalLoads } }

    val login by sessionManager.userLogin.collectAsState()

    // Pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulseAnimation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnimation"
    )

    // Función para cuando una tarea se completa correctamente
    fun onTaskSuccess() {
        completedLoads += 1
        Log.d("SPLASH", "Tarea completada. Progress: $completedLoads/$totalLoads")
        if (completedLoads == totalLoads) {
            isLoading = false
            onReady()
        }
    }

    // Función para manejar errores - ahora navega automáticamente al login
    fun onTaskError(error: String) {
        Log.e("SPLASH", "Error en carga de datos: $error")
        // Limpiamos la sesión y navegamos directamente al login
        sessionManager.clearSession()
        navController.navigate(AppRoute.LoginRoute) {
            popUpTo(AppRoute.LoginRoute) { inclusive = true }
            launchSingleTop = true
        }
    }

    LaunchedEffect(Unit) {
        Log.d("BOLETOS", "Login desde Splash: $login")

        try {
            // Cargar boletos
            fetchManager.getBoletos(
                onError = { onTaskError(it) },
                onSuccess = {
                    Log.d("BOLETOS", "Boletos desde Splash: $it")
                    onTaskSuccess()
                }
            )

            // Cargar rutas
            fetchManager.getRutas(
                onError = { onTaskError(it) },
                onSuccess = {
                    Log.d("RUTAS", "Rutas cargadas correctamente")
                    onTaskSuccess()
                }
            )

            // Cargar tarifas
            fetchManager.getTarifas(
                onError = { onTaskError(it) },
                onSuccess = {
                    Log.d("TARIFAS", "Tarifas cargadas correctamente")
                    onTaskSuccess()
                }
            )

            // Cargar paraderos
            fetchManager.getParaderos(
                onError = { onTaskError(it) },
                onSuccess = {
                    Log.d("PARADEROS", "Paraderos cargados correctamente")
                    onTaskSuccess()
                }
            )
        } catch (e: Exception) {
            Log.e("SPLASH", "Error general: ${e.message}")
            onTaskError("Error inesperado: ${e.message}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF8db8d9), Color(0xFF0066a8))
                )
            ), contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Logo with pulse animation
                Image(
                    painter = painterResource(id = R.drawable.logoweb),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .scale(scale)
                        .padding(bottom = 32.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Cargando datos...",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    "${completedLoads}/${totalLoads}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = MaterialTheme.shapes.large
                        )
                        .height(8.dp)
                )
            }
        }
    }
}