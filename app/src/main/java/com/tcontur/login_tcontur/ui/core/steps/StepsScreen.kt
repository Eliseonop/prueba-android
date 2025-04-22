package com.tcontur.login_tcontur.ui.core.steps

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun StepsScreen(
    navController: NavController,
    viewModel: StepsViewModel = viewModel()
) {
    val state by viewModel.state

    LaunchedEffect(state.allStepsCompleted) {
        if (state.allStepsCompleted) {
            delay(1000) // opcional para mostrar que todo está ok
            navController.navigate("home") {
                popUpTo("steps") { inclusive = true }
            }
        }
    }

    Column(Modifier.padding(16.dp)) {
        StepItem("Paso 1: Verificar versión", state.versionChecked) {
            viewModel.checkVersion()
        }
        StepItem("Paso 2: Permiso de ubicación", state.locationPermissionGranted) {
            viewModel.grantLocationPermission()
        }
        StepItem("Paso 3: Obtener ubicación", state.locationObtained) {
            viewModel.obtainLocation()
        }
    }
}