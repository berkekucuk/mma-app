package com.berkekucuk.mmaapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import mmaapp.composeapp.generated.resources.Montserrat_Bold
import mmaapp.composeapp.generated.resources.Montserrat_Medium
import mmaapp.composeapp.generated.resources.Montserrat_Regular
import mmaapp.composeapp.generated.resources.Montserrat_SemiBold
import mmaapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

object AppFonts {

    val Montserrat: FontFamily
        @Composable
        get() = FontFamily(
            Font(Res.font.Montserrat_Regular, FontWeight.Normal),
            Font(Res.font.Montserrat_Medium, FontWeight.Medium),
            Font(Res.font.Montserrat_SemiBold, FontWeight.SemiBold),
            Font(Res.font.Montserrat_Bold, FontWeight.Bold),
        )
}

