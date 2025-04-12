package com.tcontur.login_tcontur.ui.core.venta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.tcontur.login_tcontur.ui.data.RetrofitClient
import com.tcontur.login_tcontur.ui.data.viewmodel.boletos.Boleto
import com.tcontur.login_tcontur.ui.core.AppRoute
import com.tcontur.login_tcontur.ui.core.login.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoletosScreen(
    navController: NavController
) {


    var errorMessage by remember { mutableStateOf("") }
    var boletos by remember { mutableStateOf(emptyList<Boleto>()) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoadingBoletos by remember { mutableStateOf(true) }
    var selectedBoleto by remember { mutableStateOf<Boleto?>(null) }
    var quantity by remember { mutableStateOf(1) }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val sessionManager = SessionManager(LocalContext.current)

    LaunchedEffect(Unit) {
        val empresa = sessionManager.getEmpresa()?.codigo ?: ""
        val boletoService = RetrofitClient.getBoletoService(empresa)
        val token = sessionManager.getAuthToken()
        if (boletoService != null) {
            isLoadingBoletos = false
            boletoService.getBoletos(token)
                .enqueue(object : Callback<List<Boleto>> {
                    override fun onResponse(
                        call: Call<List<Boleto>>,
                        response: Response<List<Boleto>>
                    ) {
                        if (response.isSuccessful) {
                            boletos = response.body().orEmpty()
                                .sortedBy { it.orden }
                                .filter { it.activo }
                            selectedBoleto = boletos.firstOrNull()
                        } else {
                            errorMessage = "No se pudieron cargar los boletos"
                            showDialog = true
                        }
                    }

                    override fun onFailure(call: Call<List<Boleto>>, t: Throwable) {
                        isLoadingBoletos = false
                        errorMessage = "Error al conectar con el servidor: ${t.localizedMessage}"
                        showDialog = true
                    }
                })
        } else {
            errorMessage = "Error interno en la configuración de red"
            showDialog = true
        }
    }
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp, // Se oculta cuando está colapsado
        sheetContent = {

            if (selectedBoleto != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Se muestra el boleto seleccionado
                    BoletoCard(boleto = selectedBoleto!!, onClick = { /* No interactivo */ })
                    Spacer(modifier = Modifier.height(16.dp))
                    // Formulario para modificar cantidad
                    Text(
                        text = "Cantidad",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { if (quantity > 0) quantity-- },
                            enabled = quantity > 0,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = if (quantity > 0) Color(0xFF6200EE) else Color(
                                    0xFFBDBDBD
                                ),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Disminuir"
                            )

                        }

                        Text(
                            text = "$quantity",
                            fontSize = 70.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = { quantity++ },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(0xFF6200EE),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aumentar"
                            )
                        }
                    }
                    // Cálculo del total (tarifa * cantidad)
                    val total = selectedBoleto!!.tarifa * quantity
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total: S/",
                            fontSize = 40.sp,
                        )
                        Text(
                            text = "$total",
                            fontSize = 50.sp,
                            style = TextStyle(
                                color = Color(0xFF0066a8),
                            ),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Botón para confirmar o guardar la selección
                    Button(
                        onClick = {
                            // Aquí podrás usar:
                            // selectedBoleto!!.id, quantity, total, etc.
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0066a8),
                            contentColor = Color.White,
                        )
                    ) {
                        Text(
                            "Confirmar", fontSize = 35.sp,
                            style = TextStyle(
                                color = Color.White,
                            )
                        )

                    }
                    Spacer(modifier = Modifier.height(80.dp))

                }
            } else {
                // En caso de que no haya ningún boleto seleccionado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay boleto seleccionado")
                }
            }
        }
    ) { innerPadding ->
        FeitianSdkScreen()
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(
//                    Brush.verticalGradient(
//                        colors = listOf(Color(0xFF536976), Color(0xFF292E49))
//                    )
//                )
//        ) {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                modifier = Modifier.fillMaxSize()
//            ) {
//                items(boletos) { boleto ->
//                    BoletoCard(boleto = boleto, onClick = {
//                        selectedBoleto = boleto
//                        quantity = 1
//                        scope.launch {
//                            scaffoldState.bottomSheetState.expand()
//                        }
//                    })
//                }
//            }
//        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Error") },
            text = { Text(text = errorMessage, color = Color.Red) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        sessionManager.clearSession()
                        navController.navigate(AppRoute.LoginRoute) {
                            popUpTo(AppRoute.LoginRoute) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text("OK", color = Color.Black)
                }
            }
        )
    }
}

@Composable
fun BoletoCard(
    boleto: Boleto,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .padding(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Color(android.graphics.Color.parseColor(boleto.color))
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                text = "${boleto.tarifa}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = boleto.nombre, style = MaterialTheme.typography.bodyLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

        }
    }
}

@Preview
@Composable
fun PreviewBoletosScreen() {
    BoletosScreen(navController = NavController(LocalContext.current))
}