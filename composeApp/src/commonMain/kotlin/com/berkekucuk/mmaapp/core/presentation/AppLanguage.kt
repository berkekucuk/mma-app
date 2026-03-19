package com.berkekucuk.mmaapp.core.presentation

import androidx.compose.runtime.compositionLocalOf

enum class AppLanguage { EN, TR }

val LocalAppLanguage = compositionLocalOf { AppLanguage.EN }
val LocalAppStrings = compositionLocalOf { EnStrings }
