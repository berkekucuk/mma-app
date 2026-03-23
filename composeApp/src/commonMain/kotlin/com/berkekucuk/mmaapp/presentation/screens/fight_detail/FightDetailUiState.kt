package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.domain.model.Fighter
import kotlin.time.Instant

data class FightDetailUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val eventName: String? = null,
    val eventDate: Instant? = null,
    val fight: Fight? = null,
    val redFighter: Fighter? = null,
    val blueFighter: Fighter? = null,
    val error: FightDetailError? = null,
)

enum class FightDetailError {
    NETWORK_ERROR,
    UNKNOWN_ERROR
}