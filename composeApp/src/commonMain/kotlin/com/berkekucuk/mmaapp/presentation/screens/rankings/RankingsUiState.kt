package com.berkekucuk.mmaapp.presentation.screens.rankings

import com.berkekucuk.mmaapp.domain.model.WeightClassRanking

data class RankingsUiState(
    val isLoading: Boolean = true,
    val rankings: List<WeightClassRanking> = emptyList(),
    val expandedWeightClasses: Set<String> = emptySet(),
    val error: String? = null
)