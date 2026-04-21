package com.berkekucuk.mmaapp.presentation.screens.favorite_fighters

import com.berkekucuk.mmaapp.domain.model.RankedFighter

data class FavoriteFightersUiState(
    val fighters: List<RankedFighter> = emptyList(),
    val isOwner: Boolean = false,
    val error: FavoriteFightersError? = null,
)

enum class FavoriteFightersError {
    NETWORK_ERROR,
    UNKNOWN_ERROR,
}