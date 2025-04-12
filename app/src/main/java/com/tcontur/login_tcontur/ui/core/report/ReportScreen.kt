package com.tcontur.login_tcontur.ui.core.report

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavController
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Reportes") },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }
            },
//            actions = {
//                IconButton(onClick = { }) {
//                    Icon(
//                        imageVector = Icons.Default.Menu,
//                        contentDescription = "Menú"
//                    )
//                }
//            }
        )
    })
    { paddingValues ->

        val datosPrueba = listOf(
            // Datos para el 21 de febrero de 2025
            Datos("2025-02-21", 2.5, 3, 7.5),   // 2.5 * 3 = 7.5
            Datos("2025-02-21", 3.1, 2, 6.2),    // 3.1 * 2 = 6.2
            Datos("2025-02-21", 1.8, 4, 7.2),    // 1.8 * 4 = 7.2
            Datos("2025-02-21", 2.9, 5, 14.5),   // 2.9 * 5 = 14.5

            // Datos para el 22 de febrero de 2025
            Datos("2025-02-22", 1.5, 2, 3.0),    // 1.5 * 2 = 3.0
            Datos("2025-02-22", 2.2, 3, 6.6),    // 2.2 * 3 = 6.6
            Datos("2025-02-22", 1.0, 1, 1.0),    // 1.0 * 1 = 1.0
            Datos("2025-02-22", 2.5, 4, 10.0),   // 2.5 * 4 = 10.0

            // Datos para el 23 de febrero de 2025
            Datos("2025-02-23", 2.0, 2, 4.0),    // 2.0 * 2 = 4.0
            Datos("2025-02-23", 3.5, 3, 10.5),   // 3.5 * 3 = 10.5
            Datos("2025-02-23", 1.7, 2, 3.4),    // 1.7 * 2 = 3.4
            Datos("2025-02-23", 4.0, 5, 20.0),   // 4.0 * 5 = 20.0

            // Datos para el 24 de febrero de 2025
            Datos("2025-02-24", 3.0, 2, 6.0),    // 3.0 * 2 = 6.0
            Datos("2025-02-24", 1.5, 3, 4.5),    // 1.5 * 3 = 4.5
//            Datos("2025-02-24", 2.5, 4, 10.0),   // 2.5 * 4 = 10.0
//            Datos("2025-02-24", 4.5, 1, 4.5)     // 4.5 * 1 = 4.5
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Resumen()
                TransaccionesScreen(datosPrueba)
            }
        }
    }


}

@Preview
@Composable
fun ReportScreenPreview() {

    ReportScreen(
        navController = NavController(
            context = LocalContext.current
        )
    )
}

data class Datos(
    val fecha: String,   // Seguimos usando la fecha para filtrar
    val tarifa: Double,
    val cantidad: Int,
    val total: Double
)

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DateSelector(
//    selectedDate: String,
//    onDateSelected: (String) -> Unit
//) {
//
//}


@Composable
fun TablaDatos(datos: List<Datos>) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            // Encabezado de la tabla (sin columna de fecha)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                Text(text = "Fecha", modifier = Modifier.weight(1f), color = Color.Black)
                Text(text = "Tarifa", modifier = Modifier.weight(1f), color = Color.Black)
                Text(text = "Cantidad", modifier = Modifier.weight(1f), color = Color.Black)
                Text(text = "Total", modifier = Modifier.weight(1f), color = Color.Black)
            }
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            LazyColumn {
                items(datos) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = item.fecha, modifier = Modifier.weight(1f))
                        Text(text = item.tarifa.toString(), modifier = Modifier.weight(1f))
                        Text(text = item.cantidad.toString(), modifier = Modifier.weight(1f))
                        Text(text = item.total.toString(), modifier = Modifier.weight(1f))
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogSample(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    openDialog: MutableState<Boolean>,
    onDateSelected: (LocalDate, String) -> Unit,
    datePickerState: DatePickerState,
) {

    if (
        openDialog.value
    ) {
//        val datePickerState = rememberDatePickerState(
////            initialDisplayMode = DisplayMode.Picker
//        )
//        val confirmEnabled = remember {
//            derivedStateOf { datePickerState.selectedDateMillis != null }
//        }
        DatePickerDialog(
//            properties = DialogProperties(
//                dismissOnBackPress = true,
//                dismissOnClickOutside = true
//            ),
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Si se ha seleccionado una fecha, la formateamos y la enviamos al callback
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Convertimos el timestamp usando Calendar con zona UTC
                            val selectedUtc =
                                Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                                    timeInMillis = millis
                                }
                            // Creamos un Calendar local y seteamos la fecha extraída (año, mes y día)
                            val selectedLocal = Calendar.getInstance().apply {
                                clear()
                                set(
                                    selectedUtc.get(Calendar.YEAR),
                                    selectedUtc.get(Calendar.MONTH),
                                    selectedUtc.get(Calendar.DATE)
                                )
                            }
                            // Convertimos a LocalDate (recordando que Calendar.MONTH es 0-indexado)
                            val localDate = LocalDate.of(
                                selectedLocal.get(Calendar.YEAR),
                                selectedLocal.get(Calendar.MONTH) + 1,
                                selectedLocal.get(Calendar.DATE)
                            )
                            Log.d("DatePickerDialogSample", "localDate: $localDate")
                            // Formateamos la fecha usando DateTimeFormatter
                            val formattedDate = localDate.format(
                                DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())
                            )
                            onDateSelected(localDate, formattedDate)
                        }
                        openDialog.value = false
                    },
//                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(
//                modifier = Modifier.padding(16.dp),
                colors = DatePickerDefaults.colors(
                    Color.Blue
                ),
                state = datePickerState,
//                modifier = Modifier.verticalScroll(rememberScrollState()),
                showModeToggle = false,
                title = {
                    Text(
                        "Selecciona una fecha",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )
                    )
                },
                headline = {
                    Text(
                        text = "Fecha",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaccionesScreen(datos: List<Datos>) {
    val zoneId = ZoneId.of("America/Lima")
    val localDate = LocalDate.now(zoneId)
    val initialMillis = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
    val currentFormattedDate = localDate.format(
        DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())
    )

    var selectedDateObj by remember { mutableStateOf(localDate) }
    var selectedDateFormatted by remember { mutableStateOf(currentFormattedDate) }
    val openDialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(
                text = "Transacciones",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                ),
            )
            DatePickerDialogSample(
                openDialog = openDialog,
                onDateSelected = { date, formatted ->
                    // Actualizamos ambos estados
                    selectedDateObj = date
                    selectedDateFormatted = formatted
                },
                datePickerState = datePickerState,
            )
            Box(
                modifier = Modifier
                    .clickable { openDialog.value = true }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = selectedDateFormatted,
                        style = TextStyle(
                            fontSize = 20.sp
                        ),
                    )
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Icono de fecha",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .height(24.dp),

                        )
                }
            }
        }
        // La tabla filtra los datos por fecha seleccionada (si se eligió alguna)
        var format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
            Date.from(
                selectedDateObj.atStartOfDay(zoneId).toInstant()
            )
        )
        Log.d("ReportScreen", "format: $format")
        TablaDatos(
            datos = datos.filter {
                it.fecha == SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    Date.from(
                        selectedDateObj.atStartOfDay(zoneId).toInstant()
                    )
                )
            }
        )
    }
}