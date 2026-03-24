package com.berkekucuk.mmaapp.presentation.screens.rankings

import com.berkekucuk.mmaapp.domain.model.Ranking

data class RankingUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val mensRankings: Map<String, List<Ranking>> = emptyMap(),
    val womensRankings: Map<String, List<Ranking>> = emptyMap(),
    val error: RankingError? = null,
)

enum class RankingError {
    NETWORK_ERROR,
    UNKNOWN_ERROR,
}