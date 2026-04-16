package com.berkekucuk.mmaapp.core.presentation.colors

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No color scheme provided!")
}