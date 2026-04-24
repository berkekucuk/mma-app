package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.domain.model.Fighter

data class FightDetailUiState(
    val isRefreshing: Boolean = false,
    val fight: Fight? = null,
    val redFighter: Fighter? = null,
    val blueFighter: Fighter? = null,
    val error: FightDetailError? = null,
    val isNotificationEnabled: Boolean = false,
    val isSubmittingPrediction: Boolean = false,
    val predictedWinnerId: String? = null,
    val showPredictionBoard: Boolean = false,
)

enum class FightDetailError {
    NETWORK_ERROR,
    UNKNOWN_ERROR,
    NOT_AUTHENTICATED,
    ODDS_NOT_PUBLISHED,
    EVENT_OVER,
    FIGHT_OVER,
    FIGHT_PENDING,
}