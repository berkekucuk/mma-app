package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable
import com.berkekucuk.mmaapp.domain.enums.WeightClass

@Immutable
data class Fight(
    val fightId: String,
    val eventId: String,
    val methodType: String,
    val methodDetail: String,
    val roundSummary: String,
    val boutType: String,
    val weightClassLbs: Int?,
    val weightClass: WeightClass,
    val roundsFormat: String,
    val fightOrder: Int,
    val participants: List<Participant>
) {
    val redCorner: Participant?
        get() = participants.find { it.isRedCorner }

    val blueCorner: Participant?
        get() = participants.find { !it.isRedCorner }
}