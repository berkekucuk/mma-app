package com.berkekucuk.mmaapp.core.presentation

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColors {
    val topBarBackground = Color(0xFF1A1A1A)
    val pagerBackground = Color.Black
    val dropdownMenuBackground = Color(0xFF333333)
    val textPrimary = Color.White
    val textSecondary = Color(0xFFA3A3A3)
    val dateColor = Color(0xFFB0B0B0)
    val ufcRed = Color(0xFFD30102)
    val winnerFrame = Color(0xFF33A854)
    val redGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF8B2E2E),
            Color(0xFF4A1F1F),
            Color(0xFF1E1E1E)
        )
    )
}