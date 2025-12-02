package com.berkekucuk.mmaapp.presentation.home

import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.model.FightCardDomain

data class HomeUiState(
    val events: List<Event> = emptyList(),
    val fights: List<FightCardDomain> = emptyList(),
    val selectedIndex: Int = 0,
    val initialIndex: Int? = null,
    val isFightsLoading: Boolean = false,
    val errorMessage: String? = null
)

