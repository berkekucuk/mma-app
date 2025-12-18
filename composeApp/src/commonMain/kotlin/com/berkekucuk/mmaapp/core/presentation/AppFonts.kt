package com.berkekucuk.mmaapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.barlow_condensed_bold
import mmaapp.composeapp.generated.resources.barlow_condensed_medium
import mmaapp.composeapp.generated.resources.barlow_condensed_regular
import mmaapp.composeapp.generated.resources.barlow_condensed_semibold
import mmaapp.composeapp.generated.resources.oswald_bold
import mmaapp.composeapp.generated.resources.oswald_medium
import mmaapp.composeapp.generated.resources.oswald_regular
import mmaapp.composeapp.generated.resources.oswald_semibold
import mmaapp.composeapp.generated.resources.roboto_condensed_bold
import mmaapp.composeapp.generated.resources.roboto_condensed_medium
import mmaapp.composeapp.generated.resources.roboto_condensed_regular
import mmaapp.composeapp.generated.resources.roboto_condensed_semibold
import org.jetbrains.compose.resources.Font

object AppFonts {

    val Oswald: FontFamily
        @Composable
        get() = FontFamily(
            Font(Res.font.oswald_regular, FontWeight.Normal),
            Font(Res.font.oswald_medium, FontWeight.Medium),
            Font(Res.font.oswald_semibold, FontWeight.SemiBold),
            Font(Res.font.oswald_bold, FontWeight.Bold)
        )

    val BarlowCondensed: FontFamily
        @Composable
        get() = FontFamily(
            Font(Res.font.barlow_condensed_regular, FontWeight.Normal),
            Font(Res.font.barlow_condensed_medium, FontWeight.Medium),
            Font(Res.font.barlow_condensed_semibold, FontWeight.SemiBold),
            Font(Res.font.barlow_condensed_bold, FontWeight.Bold)
        )

    val RobotoCondensed: FontFamily
        @Composable
        get() = FontFamily(
            Font(Res.font.roboto_condensed_regular, FontWeight.Normal),
            Font(Res.font.roboto_condensed_medium, FontWeight.Medium),
            Font(Res.font.roboto_condensed_semibold, FontWeight.SemiBold),
            Font(Res.font.roboto_condensed_bold, FontWeight.Bold)
        )
}

