package com.berkekucuk.mmaapp.presentation

import com.berkekucuk.mmaapp.domain.model.Event

data class EventListUiState(
    val events: List<Event> = emptyList(),
    val selectedEventId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)