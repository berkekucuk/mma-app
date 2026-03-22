package com.berkekucuk.mmaapp.core.presentation

import androidx.compose.runtime.staticCompositionLocalOf

enum class MeasurementUnit { METRIC, IMPERIAL }

val LocalMeasurementUnit = staticCompositionLocalOf { MeasurementUnit.METRIC }
