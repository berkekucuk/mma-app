package com.berkekucuk.mmaapp.presentation.screens.favorite_fighters

import com.berkekucuk.mmaapp.domain.model.RankedFighter

data class FavoriteFightersUiState(
    val fighters: List<RankedFighter> = emptyList(),
)
