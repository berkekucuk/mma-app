package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable
import com.berkekucuk.mmaapp.domain.enums.WeightClass

@Immutable
data class Fight(
    val method: String,
    val methodDetail: String,
    val roundSummary: String,
    val weightClass: WeightClass,
    val participants: List<Participant>
) {
    val redCorner: Participant?
        get() = participants.find { it.isRedCorner == true }

    val blueCorner: Participant?
        get() = participants.find { it.isRedCorner == false }
}