package com.berkekucuk.mmaapp.presentation.screens.fighter_search

import com.berkekucuk.mmaapp.domain.model.Fighter

data class FighterSearchUiState(
    val query: String = "",
    val results: List<Fighter> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: FighterSearchError? = null,
)

enum class FighterSearchError {
    NETWORK_ERROR,
    UNKNOWN_ERROR,
}