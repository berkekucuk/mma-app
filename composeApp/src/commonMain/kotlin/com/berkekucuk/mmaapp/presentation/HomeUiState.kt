package com.berkekucuk.mmaapp.presentation

import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.model.FightCardDomain

data class HomeUiState(
    val events: List<Event> = emptyList(),
    val selectedEvent: Event? = null,
    val isLoadingEvents: Boolean = false,

    val fightCards: List<FightCardDomain> = emptyList(),
    val isLoadingFights: Boolean = true,

    val error: String? = null
)