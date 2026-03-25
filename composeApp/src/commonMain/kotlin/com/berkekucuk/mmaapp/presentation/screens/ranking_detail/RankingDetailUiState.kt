package com.berkekucuk.mmaapp.presentation.screens.ranking_detail

import com.berkekucuk.mmaapp.domain.model.RankedFighter

data class RankingDetailUiState(
    val isLoading: Boolean = true,
    val weightClassId: String = "",
    val weightClassName: String = "",
    val rankedFighters: List<RankedFighter> = emptyList(),
    val isRefreshing: Boolean = false
)
