package com.berkekucuk.mmaapp.presentation.screens.home

sealed interface HomeUiAction {
    data class OnEventClicked(val eventId: String) : HomeUiAction
    data class OnYearSelected(val year: Int) : HomeUiAction
    data object OnRefreshFeaturedTab : HomeUiAction
    data object OnRefreshUpcomingTab : HomeUiAction
    data object OnRefreshCompletedTab : HomeUiAction
}

sealed interface NavigationEvent {
    data class ToEventDetail(val eventId: String) : NavigationEvent
    data object Back : NavigationEvent
}
