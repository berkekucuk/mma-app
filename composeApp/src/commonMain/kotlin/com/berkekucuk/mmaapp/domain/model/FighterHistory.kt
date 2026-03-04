package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable
import kotlin.time.Instant

@Immutable
data class FighterHistory(
    val eventName: String,
    val eventDate: Instant?,
    val methodType: String,
    val methodDetail: String,
    val roundSummary: String,
    val boutType: String,
    val weightClassLbs: Int?,
    val roundsFormat: String,
    val weightClassId: String,
    val oddsValue: Int?,
    val oddsLabel: String,
    val result: String,
    val recordAfterFight: String,
    val isRedCorner: Boolean,
    val oppFighterId: String,
    val oppName: String,
    val oppImageUrl: String,
    val oppRecord: Record,
    val oppFightingOutOf: String,
    val oppHeight: Measurement,
    val oppReach: Measurement,
    val oppDateOfBirth: String,
    val oppCountryCode: String,
    val oppOddsValue: Int?,
    val oppOddsLabel: String,
    val oppResult: String,
    val oppRecordAfterFight: String,
    val oppIsRedCorner: Boolean,
)
