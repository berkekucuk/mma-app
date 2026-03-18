package com.berkekucuk.mmaapp.presentation.screens.rankings

import com.berkekucuk.mmaapp.domain.model.Ranking
import com.berkekucuk.mmaapp.domain.model.WeightClass

data class RankingUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val mensRankings: Map<WeightClass, List<Ranking>> = emptyMap(),
    val womensRankings: Map<WeightClass, List<Ranking>> = emptyMap()
)