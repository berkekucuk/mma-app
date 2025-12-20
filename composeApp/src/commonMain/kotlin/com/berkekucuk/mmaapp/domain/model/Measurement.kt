package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Measurement(
    val metric: Int?,
    val imperial: String?
) {
    val displayValue: String
        get() = imperial ?: metric?.toString() ?: ""

    companion object {
        val EMPTY = Measurement(metric = null, imperial = null)
    }
}

