package com.berkekucuk.mmaapp.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColors {
    val TopBarBackground = Color(0xFF1A1A1A)
    val PagerBackground = Color.Black
    val TextPrimary = Color.White
    val TextSecondary = Color(0xFFA3A3A3)
    val UfcRed = Color(0xFFD30102)
    val CardBackground = Color(0xFF2A2A2A)
    val winner = Color(0xFF33A854)
    val RedGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF8B2E2E),
            Color(0xFF4A1F1F),
            Color(0xFF1E1E1E)
        )
    )
}
