package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Measurement(
    val metric: Int?,
    val imperial: String?
) {
    companion object {
        val EMPTY = Measurement(metric = null, imperial = null)
    }
}