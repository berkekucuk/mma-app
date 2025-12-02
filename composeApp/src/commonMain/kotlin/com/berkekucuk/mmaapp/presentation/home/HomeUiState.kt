package com.berkekucuk.mmaapp.presentation.home

import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.model.FightCardDomain

data class HomeUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val fights: List<FightCardDomain> = emptyList(),
    val selectedIndex: Int = 0,
    val errorMessage: String? = null
)

