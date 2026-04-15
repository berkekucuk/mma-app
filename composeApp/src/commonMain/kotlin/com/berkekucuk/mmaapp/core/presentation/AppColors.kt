package com.berkekucuk.mmaapp.core.presentation

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColors {
    val topBarBackground = Color(0xFF1A1A1A)
    val pagerBackground = Color.Black
    val dropdownMenuBackground = Color(0xFF2A2A2A)
    val textPrimary = Color(0xFFECEDEF)
    val textSecondary = Color(0xFF8B8E90)
    val dateColor = Color(0xFFB0B0B0)
    val ufcRed = Color(0xFFD20909)
    val winnerFrame = Color(0xFF33A854)
    val upcomingColor = Color(0xFF525252)
    val upcomingColor2 = Color(0xFFE2E2E2)
    val winColor = Color(0xFF47BE44)
    val winColor2 = Color(0xFFBAE5B6)
    val loseColor = Color(0xFFC6472A)
    val loseColor2 = Color(0xFFEAB6AC)
    val drawColor = Color(0xFF70ADFA)
    val drawColor2 = Color(0xFFC5DEFD)
    val noContestColor = Color(0xFFF9D03B)
    val noContestColor2 = Color(0xFFFBECB6)
    val signInButton = Color(0xFF4CAF50)
    val googleSignInButtonText = Color(0xFF1F1F1F)
    val white = Color.White
    val black = Color.Black
    val fightItemBackground = Color(0xFF171C1F)
    val dividerColor = Color(0xFF2C2C2C)
    val cardHeaderBackground = Color(0xFF20232A)
    val cardBorder = Color(0xFF3E4149)
    val fighterBarBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF2C2C2C), Color(0xFF1A1A1A))
    )

    // Events Screen
    val eventDetailTopBarGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF200000), Color(0xFF150000), Color(0xFF0A0000))
    )

    // Event Detail Screen
    val eventsTopBarGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF161B1F), Color(0xFF101418), Color(0xFF0A0C0F))
    )

    // Rankings Screen
    val rankingTopBarGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1A1020), Color(0xFF14121C), Color(0xFF100E18))
    )
    val rankingChampionBadge = Brush.horizontalGradient(
        colors = listOf(Color(0xFFD4A843), Color(0xFFB8922E))
    )
    val rankingWeightClassAccent = Color(0xFF3A3A3A)
}
