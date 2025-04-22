package com.tcontur.login_tcontur.ui.core.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tcontur.login_tcontur.ui.data.manager.SessionManager

@Composable
fun QrLoad(
    sessionManager: SessionManager,
    qrButtonEnabled: Boolean = true,
    onQrClick: () -> Unit
) {
    val decodedLogin by sessionManager.protoLogin.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botón de escaneo QR (2/5)
        Button(
            onClick = onQrClick,
            enabled = qrButtonEnabled,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (qrButtonEnabled) MaterialTheme.colorScheme.primary else Color.Gray
            ),
            modifier = Modifier.weight(2f) // 2 de 5 partes
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = "Escanear QR",
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
//            Text("Escanear")
        }

        // Verificación de QR (3/5)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(3f) // 3 de 5 partes
                .background(
                    color = if (decodedLogin != null) Color(0xFFE0F7FA) else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = if (decodedLogin != null) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = if (decodedLogin != null) "QR Escaneado" else "QR no escaneado",
                tint = if (decodedLogin != null) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (decodedLogin != null) "QR escaneado" else "QR no escaneado",
                style = MaterialTheme.typography.bodyMedium,
                color = if (decodedLogin != null) Color(0xFF4CAF50) else Color(0xFFD32F2F)
            )
        }
    }
}


@Preview
@Composable
fun QrLoadPreview() {
    QrLoad(
        sessionManager = SessionManager(context = LocalContext.current),
        qrButtonEnabled = true,
        onQrClick = {}
    )
}
