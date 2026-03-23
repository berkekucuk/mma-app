package com.berkekucuk.mmaapp.presentation.screens.event_detail

import com.berkekucuk.mmaapp.domain.model.Event

data class EventDetailUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val event: Event? = null,
    val error: EventDetailError? = null,
)

enum class EventDetailError {
    NETWORK_ERROR,
    UNKNOWN_ERROR,
}