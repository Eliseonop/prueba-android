package com.tcontur.login_tcontur.ui.core.venta

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.tcontur.login_tcontur.ui.core.AppRoute

data class NavigateCard(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val route: AppRoute,
    val enabled: Boolean
)