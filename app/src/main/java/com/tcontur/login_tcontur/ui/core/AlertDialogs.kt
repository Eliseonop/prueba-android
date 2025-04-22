package com.tcontur.login_tcontur.ui.core

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    errorMessage: String,
    onOk: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Error") },
            text = { Text(text = errorMessage, color = Color.Red) },
            confirmButton = {
                Button(onClick = {
                    onOk()
                    onDismiss()
                }) {
                    Text("OK", color = Color.Black)
                }
            }
        )
    }
}

@Composable
fun WarningDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    warningMessage: String,
    onOk: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Advertencia") },
            text = { Text(text = warningMessage, color = Color.Yellow) },
            confirmButton = {
                Button(onClick = {
                    onOk()
                    onDismiss()
                }) {
                    Text("OK", color = Color.Black)
                }
            }
        )
    }
}
