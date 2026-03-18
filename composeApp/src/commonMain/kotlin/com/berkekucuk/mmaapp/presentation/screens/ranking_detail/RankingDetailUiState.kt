package com.berkekucuk.mmaapp.presentation.screens.ranking_detail

import com.berkekucuk.mmaapp.domain.model.Ranking

data class RankingDetailUiState(
    val isLoading: Boolean = true,
    val weightClassName: String = "",
    val rankedFighters: List<Ranking> = emptyList(),
    val isRefreshing: Boolean = false
)
