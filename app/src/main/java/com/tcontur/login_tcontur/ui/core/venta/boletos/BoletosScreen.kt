package com.tcontur.login_tcontur.ui.core.venta.boletos

import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.room.Room
import com.tcontur.login_tcontur.ui.core.ErrorDialog
import com.tcontur.login_tcontur.ui.data.models.Boleto
import com.tcontur.login_tcontur.ui.data.manager.SessionManager
import com.tcontur.login_tcontur.ui.core.protobin.ProtoSocketManager
import com.tcontur.login_tcontur.ui.data.db.AppDatabase
import com.tcontur.login_tcontur.ui.data.db.dao.TicketDao
import com.tcontur.login_tcontur.ui.data.db.entity.BoletoCountEntity
import com.tcontur.login_tcontur.ui.data.db.entity.TicketEntity
import com.tcontur.login_tcontur.ui.data.manager.DeviceManager
import com.tcontur.login_tcontur.ui.data.manager.FetchManager
import com.tcontur.login_tcontur.ui.data.manager.PrinterManager
import com.tcontur.login_tcontur.ui.data.models.LoginResponse
import com.tcontur.login_tcontur.ui.data.models.QrData
import com.tcontur.login_tcontur.ui.data.models.Zona
import com.tcontur.login_tcontur.ui.data.models.TicketData
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

//@Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoletosScreen(
    navController: NavController,
    sessionManager: SessionManager,
    fetchManager: FetchManager,
    protoSocket: ProtoSocketManager,
) {
    val context = LocalContext.current
    var boletos by remember { mutableStateOf(emptyList<Boleto>()) }
    var selectedBoleto by remember { mutableStateOf<Boleto?>(null) }
    var quantity by remember { mutableIntStateOf(1) }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val printer = PrinterManager(DeviceManager.printer)
    var isPrinting by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val actual by sessionManager.actual.collectAsState()

    var final by remember { mutableStateOf<Zona?>(null) }
    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "tickets"
    ).fallbackToDestructiveMigration(true).build()
    val ticketDao = db.ticketDao()
    val boletoCountDao = db.boletoCountDao()
    val counters by sessionManager.counters.collectAsState()


    fun obtenerFinal(boleto: Boleto) {
        scope.launch {
            combine(
                sessionManager.actual,
                fetchManager.tarifasFlow
            ) { actual, tarifas ->
                if (actual !== null) {

                    Log.d("Paraderos", "Paraderos: $actual")
                    Log.d("BOLETOS", "Tarifas: $tarifas")

                    tarifas.find {
                        it.inicio.id == actual.id && it.boleto.id == boleto.id
                    }
                } else {
                    null
                }

            }.collect { tarifa ->
                final = tarifa?.fin
            }
        }
    }


    suspend fun generateTicketsWithCounter(
        boleto: Boleto,
        quantity: Int,
        user: LoginResponse,
        qr: QrData,
    ): List<TicketEntity> {
        val decoded = sessionManager.protoLogin.value
        val existingCounter = boletoCountDao.getCounter(boleto.id)
        val startingNumero = (existingCounter?.lastNumero ?: 0) + 1

        // Generamos tickets con numeración consecutiva
        if (decoded == null || user == null || qr == null) {
            showDialog = true
            message = "Error al generar tickets: Datos inválidos"
        }
        val tickets = (0 until quantity).map { index ->
            TicketEntity(
                fare = boleto.id,
                driver = user.id,
                day = LocalDate.parse(qr.dia),
                busstopEnd = final?.id,
                time = LocalDateTime.now(),
                busstopStart = actual?.id,
                inspector = null,
                paymentMethod = PaymentMethod.QR.code,
                numero = startingNumero + index,
                price = boleto.tarifa * 100,
                route = decoded?.route,
                session = qr.sesion,
                vehicle = decoded?.id,
                card = null
            )
        }

        val insertedIds = mutableListOf<Long>()

        tickets.forEach { ticket ->
            val id = ticketDao.insert(
                ticket
            )
            insertedIds.add(id)
        }
        val insertedTickets = ticketDao.getTicketsByCorrelatives(insertedIds)

        sessionManager.updateCounter(boleto, startingNumero + quantity - 1, boletoCountDao)
        return insertedTickets
    }




    LaunchedEffect(selectedBoleto) {
        selectedBoleto?.let { obtenerFinal(it) }
    }



    LaunchedEffect(Unit) {
        sessionManager.loadAllCounters(boletoCountDao)

        scope.launch {
            val tickets = ticketDao.getAll()
            Log.d("BOLETOS", "Tickets vendidos: $tickets")
        }

        fetchManager.boletosFlow.collect { lista ->
            boletos = lista
            selectedBoleto = lista.firstOrNull()
        }

    }

    fun confirmarBoleto(boleto: Boleto, quantity: Int = 1) {
        val decoded = sessionManager.protoLogin.value
        val user = sessionManager.userLogin.value
        val qr = sessionManager.qrInfo.value
        val inicio = sessionManager.actual.value

        Log.d("BOLETOS", "Login decoded: $decoded")
        Log.d("BOLETOS", "Login user: $user")
        Log.d("BOLETOS", "QR: $qr")
        Log.d("BOLETOS", "Inicio: $inicio")

        if (decoded != null && user != null && qr != null && inicio != null) {
            isPrinting = true

            scope.launch {
                try {
                    val tickets = generateTicketsWithCounter(
                        boleto = boleto,
                        quantity = quantity,
                        user = user,
                        qr = qr
                    )

                    Log.d("BOLETOS", "Tickets generados: $tickets")

                    // Aquí puedes manejar la lógica posterior (como impresión)
                    val empresa = sessionManager.getEmpresa()
                    val ticketData = TicketData(
                        empresa = empresa?.nombre,
                        ruc = empresa?.codigo,
                        concepto = boleto.tarifa.toString(),
                        monto = boleto.tarifa.toString(),
                        unidad = user.nombre,
                        metodoPago = PaymentMethod.QR.code.toString(),
                        ticket = boleto.tarifa.toString(),
                        hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                        fecha = qr.dia.toString(),
                        id = tickets.firstOrNull()?.correlative.toString(),
                        ruta = boleto.ruta?.codigo,
                    )

//                    sessionManager.updateCounter()

//                    printer.print(tickets, ticketData)
                    message = "Tickets generados correctamente"
//                    sessionManager.registrarBoletoVendido(boleto.id, quantity)

                    // Creación del ticketMap
                    val ticketMap: Map<String, List<Map<String, Any?>>> = mapOf(
                        "tickets" to tickets.map { it.toMap() }
                    )

                    Log.d("BOLETOS", "TicketMap: $ticketMap")

                    protoSocket.send(ticketMap, "tickets")

                    printer.imprimirTicket(
                        ticketData,
                        onSuccess = {
                            isPrinting = false
                            Log.d("BOLETOS", "Impresión exitosa")
//                            sessionManager.registrarBoletoVendido(boleto.id, quantity)
                        },
                        onError = { code, error ->
                            isPrinting = false
                            Log.d("BOLETOS", "Error de impresión: $code $error")
                            message = "Error de impresión: $code $error"
                            showDialog = true
                        }
                    )

                } catch (e: Exception) {
                    Log.e("BOLETOS", "Error al generar tickets: ${e.message}")
                    message = "Error al generar tickets: ${e.message}"
                } finally {
                    isPrinting = false
                }
            }


        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp, // Se oculta cuando está colapsado
        sheetContent = {
            if (
                isPrinting
            ) {
                PrintingStatus(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .padding(16.dp),

                    )
            } else if (selectedBoleto != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BusRouteComponent(
                        inicio = actual?.nombre ?: "",
                        final = final?.nombre ?: "Ultimo",
                        labelFinal = "Paradero Pendiente",
                        loading = false
                    )
                    ErrorDialog(
                        showDialog = showDialog,
                        errorMessage = message,
                        onOk = {
                            showDialog = false
                        },
                        onDismiss = {
                            showDialog = false
                        }
                    )
                    // Se muestra el boleto seleccionado
                    BoletoCard(
                        boleto = selectedBoleto!!, onClick = { /* No interactivo */ },
                        cantidadVendida = null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Formulario para modificar cantidad
                    Text(
                        text = "Cantidad",
                        fontSize = 30.sp,
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
                            fontSize = 45.sp,
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
                            selectedBoleto?.let { confirmarBoleto(it, quantity) }

                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0066a8),
                            contentColor = Color.White,
                        )
                    ) {
                        Text(
                            "Confirmar", fontSize = 35.sp,
                            style = TextStyle(
                                color = Color.White,
                            ),
                        )

                    }
                    Spacer(modifier = Modifier.height(80.dp))

                }
            }

        }
    ) { innerPadding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            items(boletos) { boleto ->
                BoletoCard(
                    boleto = boleto,
//                    cantidadVendida = counters[boleto.id] ?: 0,
                    cantidadVendida = counters.find { it.fare == boleto.id }?.lastNumero ?: 0,
                    onClick = {
                        selectedBoleto = boleto
                        quantity = 1
                        obtenerFinal(boleto)
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BoletoCard(
    boleto: Boleto,
    cantidadVendida: Int?,
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
        Box(
            modifier = Modifier
                .background(
                    Color(android.graphics.Color.parseColor(boleto.color))
                )
                .fillMaxSize()
        ) {
            // Badge for showing sold count
            if (cantidadVendida !== null) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = cantidadVendida.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color(android.graphics.Color.parseColor(boleto.color)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )
                }
            }

            // Main content
            Column(
                modifier = Modifier.fillMaxSize(),
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
                    text = boleto.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}