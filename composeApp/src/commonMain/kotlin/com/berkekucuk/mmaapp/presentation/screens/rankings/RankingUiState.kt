package com.berkekucuk.mmaapp.presentation.screens.rankings

import com.berkekucuk.mmaapp.domain.model.WeightClass

data class RankingUiState(
    val isRefreshing: Boolean = false,
    val weightClasses: List<WeightClass> = emptyList(),
    val error: RankingError? = null,
)

enum class RankingError {
    NETWORK_ERROR,
    UNKNOWN_ERROR,
}