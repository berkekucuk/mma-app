package com.berkekucuk.mmaapp.presentation.screens.home

import com.berkekucuk.mmaapp.domain.model.Event

data class HomeUiState(
    val isLoading: Boolean = true,
    val isYearLoading: Boolean = false,
    val isRefreshingUpcomingTab: Boolean = false,
    val isRefreshingCompletedTab: Boolean = false,
    val selectedYear: Int? = null,
    val availableYears: List<Int> = emptyList(),
    val allEvents: List<Event> = emptyList(),
    val upcomingEvents: List<Event> = emptyList(),
    val completedEvents: List<Event> = emptyList()
)

