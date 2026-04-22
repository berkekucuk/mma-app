package com.berkekucuk.mmaapp.core.presentation

import androidx.compose.runtime.staticCompositionLocalOf

enum class ThemeMode { LIGHT, DARK }

val LocalThemeMode = staticCompositionLocalOf<ThemeMode> {
    error("No ThemeMode provided")
}