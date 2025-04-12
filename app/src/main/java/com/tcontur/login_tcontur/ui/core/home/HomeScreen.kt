package com.tcontur.login_tcontur.ui.core.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tcontur.login_tcontur.ui.core.AppRoute
import com.tcontur.login_tcontur.ui.core.login.SessionManager
import com.tcontur.login_tcontur.ui.core.venta.NavigateCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    var menuExpanded by remember { mutableStateOf(false) }
    var sessionManager = SessionManager(LocalContext.current)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Servicios") },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Salir") },
                            onClick = {
                                menuExpanded = false
                                // Navega a la pantalla que desees (por ejemplo, Login)
                                sessionManager.clearSession()
                                navController.navigate(AppRoute.LoginRoute) {
                                    popUpTo(AppRoute.LoginRoute) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            ToolsScreen(navController)
        }
    }
}

@Preview
@Composable
fun VentaScreenPreview() {
    HomeScreen(
        navController = NavController(
            context = LocalContext.current
        )
    )
}

@Composable
fun ToolsScreen(navController: NavController) {
    // Lista de items. Puedes agregar más o cambiar colores e íconos.
    val toolItems = listOf(
        NavigateCard(
            name = "Venta de boletos",
            description = "Accede al módulo de boletos",
            icon = Icons.Filled.Aod,   // Ejemplo de ícono
            color = Color(0xFFFFA726),         // Ejemplo de color naranja
            route = AppRoute.BoletoRoute
        ),
        NavigateCard(
            name = "Reportes",
            description = "Módulo de reportes",
            icon = Icons.Filled.Assessment,     // Ejemplo de ícono
            color = Color(0xFF66BB6A),         // Ejemplo de color verde
            route = AppRoute.ReportRoute
        )
        // Agrega más ToolItem conforme los necesites
    )

    // Contenedor de la lista en Compose (LazyColumn).
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(toolItems) { item ->
                ToolCard(item, onClick = {
                    navController.navigate(item.route)
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolCard(
    toolItem: NavigateCard,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .background(toolItem.color)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color.Blue,
            containerColor = toolItem.color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono
            Icon(
                imageVector = toolItem.icon,
                contentDescription = toolItem.name,
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = toolItem.name,
//                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 30.sp,
                    )
                )
                Text(
                    text = toolItem.description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )
            }
        }
    }
}

@Preview
@Composable
fun ToolCardPreview() {

}