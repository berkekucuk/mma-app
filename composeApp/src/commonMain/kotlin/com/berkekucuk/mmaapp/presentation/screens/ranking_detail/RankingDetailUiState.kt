package com.berkekucuk.mmaapp.presentation.screens.ranking_detail

import com.berkekucuk.mmaapp.domain.model.Ranking

data class RankingDetailUiState(
    val isLoading: Boolean = true,
    val weightClassId: String = "",
    val weightClassName: String = "",
    val rankedFighters: List<Ranking> = emptyList(),
    val isRefreshing: Boolean = false
) {
    val isPoundForPound: Boolean
        get() = weightClassId == "mens_p4p" || weightClassId == "womens_p4p"
}
