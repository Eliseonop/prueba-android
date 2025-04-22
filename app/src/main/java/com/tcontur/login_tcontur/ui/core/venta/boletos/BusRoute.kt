package com.tcontur.login_tcontur.ui.core.venta.boletos

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BusRouteComponent(
    inicio: String = "",
    final: String = "",
    labelInicio: String = "Paradero Actual",
    labelFinal: String = "Paradero Siguiente",
    loading: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = labelInicio, style = MaterialTheme.typography.labelMedium)
            Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = "Bus Icon",
                modifier = Modifier.size(60.dp),
                tint = Color.Green
            )
            if (loading) {
                PulseSkeletonBox(width = 100.dp, height = 20.dp)
//                LinearProgressIndicator(
//                    modifier = Modifier
//                        .size(width = 100.dp, height = 16.dp)
//                        .padding(top = 8.dp),
//                    color = MaterialTheme.colorScheme.primary,
//                )
            } else {
                Text(
                    text = inicio,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Arrow",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = labelFinal, style = MaterialTheme.typography.labelMedium)
            Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = "Bus Icon",
                modifier = Modifier.size(60.dp),
                tint = Color.Blue
            )
            if (loading) {
                PulseSkeletonBox(width = 100.dp, height = 20.dp)
//                LinearProgressIndicator(
//                    modifier = Modifier
//                        .size(width = 100.dp, height = 16.dp)
//                        .padding(top = 8.dp),
//                    color = MaterialTheme.colorScheme.primary,
//                )
            } else {
                Text(
                    text = final,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Composable
fun PulseSkeletonBox(width: Dp, height: Dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaAnim"
    )

    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .alpha(alpha)
            .background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.small
            )
    )
}