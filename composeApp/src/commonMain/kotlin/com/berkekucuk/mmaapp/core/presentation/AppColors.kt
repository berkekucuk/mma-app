package com.berkekucuk.mmaapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class AppColorScheme(
    val topBarBackground: Color,
    val pagerBackground: Color,
    val dropdownMenuBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val dateColor: Color,
    val ufcRed: Color,
    val winnerFrame: Color,
    val upcomingColor: Color,
    val upcomingColor2: Color,
    val winColor: Color,
    val winColor2: Color,
    val loseColor: Color,
    val loseColor2: Color,
    val drawColor: Color,
    val drawColor2: Color,
    val noContestColor: Color,
    val noContestColor2: Color,
    val fightItemBackground: Color,
    val dividerColor: Color,
    val cardHeaderBackground: Color,
    val cardBorder: Color,
    val fighterBarBackground: Brush,
    val loginGradientTop: Color,
    val loginGradientMid1: Color,
    val loginGradientMid2: Color,
    val loginGradientBottom: Color,
    val loginGlowWarm: Color,
    val loginDivider: Color,
    val loginDividerText: Color,
    val loginSubtitle: Color,
    val loginButtonBackground: Color,
    val loginButtonContent: Color,
    val eventDetailTopBarGradient: Brush,
    val eventsTopBarGradient: Brush,
    val rankingTopBarGradient: Brush,
    val rankingChampionBadge: Brush,
    val rankingWeightClassAccent: Color
)
val DarkColorScheme = AppColorScheme(
    topBarBackground = Color(0xFF1A1A1A),
    pagerBackground = Color.Black,
    dropdownMenuBackground = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFECEDEF),
    textSecondary = Color(0xFF8B8E90),
    dateColor = Color(0xFFB0B0B0),
    ufcRed = Color(0xFFD20909),
    winnerFrame = Color(0xFF33A854),
    upcomingColor = Color(0xFF525252),
    upcomingColor2 = Color(0xFFE2E2E2),
    winColor = Color(0xFF47BE44),
    winColor2 = Color(0xFFBAE5B6),
    loseColor = Color(0xFFC6472A),
    loseColor2 = Color(0xFFEAB6AC),
    drawColor = Color(0xFF70ADFA),
    drawColor2 = Color(0xFFC5DEFD),
    noContestColor = Color(0xFFF9D03B),
    noContestColor2 = Color(0xFFFBECB6),
    fightItemBackground = Color(0xFF171C1F),
    dividerColor = Color(0xFF2C2C2C),
    cardHeaderBackground = Color(0xFF20232A),
    cardBorder = Color(0xFF3E4149),
    fighterBarBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF2C2C2C), Color(0xFF1A1A1A))
    ),
    loginGradientTop = Color(0xFF1A0A0A),
    loginGradientMid1 = Color(0xFF120808),
    loginGradientMid2 = Color(0xFF0D0D12),
    loginGradientBottom = Color(0xFF0A0A10),
    loginGlowWarm = Color(0xFF1A0F20),
    loginDivider = Color(0xFF1F1F1F),
    loginDividerText = Color(0xFF3A3A3A),
    loginSubtitle = Color(0xFF6B6B6B),
    loginButtonBackground = Color.White,
    loginButtonContent = Color(0xFF1A1A1A),
    eventDetailTopBarGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF200000), Color(0xFF150000), Color(0xFF0A0000))
    ),
    eventsTopBarGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF161B1F), Color(0xFF101418), Color(0xFF0A0C0F))
    ),
    rankingTopBarGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1A1020), Color(0xFF14121C), Color(0xFF100E18))
    ),
    rankingChampionBadge = Brush.horizontalGradient(
        colors = listOf(Color(0xFFD4A843), Color(0xFFB8922E))
    ),
    rankingWeightClassAccent = Color(0xFF3A3A3A)
)

val LightColorScheme = AppColorScheme(
    topBarBackground = Color(0xFFFFFFFF),
    pagerBackground = Color(0xFFF5F5F5),
    dropdownMenuBackground = Color(0xFFE0E0E0),
    textPrimary = Color(0xFF1A1A1A),
    textSecondary = Color(0xFF5F6368),
    dateColor = Color(0xFF757575),
    ufcRed = Color(0xFFD20909),
    winnerFrame = Color(0xFF2E8B42),
    upcomingColor = Color(0xFF9E9E9E),
    upcomingColor2 = Color(0xFF616161),
    winColor = Color(0xFF47BE44),
    winColor2 = Color(0xFF1B5E20),
    loseColor = Color(0xFFC6472A),
    loseColor2 = Color(0xFF8E2211),
    drawColor = Color(0xFF70ADFA),
    drawColor2 = Color(0xFF1976D2),
    noContestColor = Color(0xFFF9D03B),
    noContestColor2 = Color(0xFFF57F17),
    fightItemBackground = Color(0xFFFFFFFF),
    dividerColor = Color(0xFFE0E0E0),
    cardHeaderBackground = Color(0xFFEEEEEE),
    cardBorder = Color(0xFFE0E0E0),
    fighterBarBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0E0E0), Color(0xFFF5F5F5))
    ),
    loginGradientTop = Color(0xFFFFEBEE),
    loginGradientMid1 = Color(0xFFFAFAFA),
    loginGradientMid2 = Color(0xFFF5F5F5),
    loginGradientBottom = Color(0xFFEEEEEE),
    loginGlowWarm = Color(0xFFFCE4EC),
    loginDivider = Color(0xFFE0E0E0),
    loginDividerText = Color(0xFF757575),
    loginSubtitle = Color(0xFF5F6368),
    loginButtonBackground = Color(0xFF1A1A1A),
    loginButtonContent = Color.White,
    eventDetailTopBarGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2), Color(0xFFEF9A9A))
    ),
    eventsTopBarGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFF5F5F5), Color(0xFFEEEEEE))
    ),
    rankingTopBarGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7), Color(0xFFCE93D8))
    ),
    rankingChampionBadge = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFBC02D), Color(0xFFF57F17))
    ),
    rankingWeightClassAccent = Color(0xFFE0E0E0)
)

val LocalAppColors = staticCompositionLocalOf<AppColorScheme> {
    error("Tema renkleri saptanmadı!")
}

object AppTheme {
    val colors: AppColorScheme
        @Composable
        get() = LocalAppColors.current
}
