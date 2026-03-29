package com.berkekucuk.mmaapp.presentation.screens.ranking_detail

import com.berkekucuk.mmaapp.domain.model.RankedFighter

data class RankingDetailUiState(
    val isRefreshing: Boolean = false,
    val weightClassId: String = "",
    val rankedFighters: List<RankedFighter> = emptyList()
)