package com.berkekucuk.mmaapp.core.presentation

import androidx.compose.runtime.compositionLocalOf

enum class AppLanguage { EN, TR }

val LocalAppStrings = compositionLocalOf { EnStrings }