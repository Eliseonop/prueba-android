package com.tcontur.login_tcontur.ui.core.report

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Aod
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class ToolItem(
    val name: String,
    val value: Int,
    val icon: ImageVector, // Asegúrate de importar el paquete correspondiente
    val color: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun Resumen() {

    var tools = listOf(
        ToolItem(
            name = "Transacciones",
            value = 10,
            icon = Icons.Default.Assessment,
            color = Color.Blue
        ),
        ToolItem(
            name = "Boletos",
            value = 30,
            icon = Icons.Default.ShoppingCart,
            color = Color.Green
        ),
//        ToolItem(
//            name = "Reporte de asistencia",
//            value = "Reporte de asistencia",
//            icon = Icons.Default.Aod,
//            color = Color.Red
//        ),
//        ToolItem(
//            name = "Reporte de eventos",
//            value = "Reporte de eventos",
//            icon = Icons.Default.Menu,
//            color = Color.Yellow
//        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tools) { toolItem ->

            InfoCard(
                title = toolItem.name,
                value = toolItem.value.toString(),
                icon = toolItem.icon,
                iconColor = toolItem.color,
                modifier = Modifier.fillMaxWidth()
            )


//            Card(
//                modifier = Modifier
//                    .fillMaxWidth() // La celda ya determina el ancho (la mitad de la pantalla)
////                    .clip(RoundedCornerShape(16.dp))
//                    .aspectRatio(2f)
//                    .clip(RoundedCornerShape(16.dp))
//                    .border(
//                        3.dp, Color.Gray,
//                        shape = RoundedCornerShape(16.dp)
//                    )
//                    .clickable { },
////                shape = RoundedCornerShape(15.dp),
//                colors = CardDefaults.cardColors(
////                        contentColor = Color.Blue,
//                    containerColor = Color.White
//                ),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(RoundedCornerShape(16.dp)),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center,
//                ) {
//
////                    Box(
////                        modifier = Modifier
////                            .background(
////                                Brush.verticalGradient(
////                                    colors = listOf(
////                                        toolItem.color.copy(alpha = 0.1f),
////                                        toolItem.color.copy(alpha = 0.5f)
////                                    )
////                                )
////                            )
////                            .clip(RoundedCornerShape(16.dp))
////                            .border(
////                                3.dp, Color.Gray,
////                                shape = RoundedCornerShape(16.dp)
////                            )
////                            .padding(10.dp)
////                    ) {
//                    Icon(
//                        imageVector = toolItem.icon,
//                        contentDescription = toolItem.name,
//                        tint = Color.Blue,
//                        modifier = Modifier
//                            .size(30.dp) // Tamaño fijo para mantener la forma circular
//                            .clip(CircleShape) // Recorta el Icono a forma circular
//                            .background(
//                                Brush.verticalGradient(
//                                    colors = listOf(
//                                        toolItem.color.copy(alpha = 0.1f),
//                                        toolItem.color.copy(alpha = 0.5f)
//                                    )
//                                )
//                            )
//                            .padding(5.dp)
//                        // Fondo para visualizar el efecto
//                    )
////                    }
//
//
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Column {
//                        Text(
//                            text = toolItem.name,
//                            style = TextStyle(
//                                color = Color.White,
//                                fontSize = 20.sp
//                            )
//                        )
//                        Text(
//                            text = toolItem.value.toString(),
//                            style = TextStyle(
//                                color = Color.White,
//                                fontSize = 20.sp
//                            )
//                        )
//                    }
//                }
//            }

        }
    }

}

@Composable
fun InfoCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White
) {
    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // El color de fondo que desees
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Texto
            Column {
                Text(
                    text = title,
                    style = TextStyle.Default.copy(
                        color = Color.Gray,
                        fontSize = 18.sp
                    ),
                )
                Text(
                    text = value,
                    style = TextStyle.Default.copy(
                        color = Color.Black,
                        fontSize = 18.sp
                    ),
                )
            }

            // Ícono con fondo circular
            Box(
                modifier = Modifier
                    .size(40.dp).width(40.dp)
                    .clip(CircleShape)
                    // Fondo circular con opacidad
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
        }
    }
}
