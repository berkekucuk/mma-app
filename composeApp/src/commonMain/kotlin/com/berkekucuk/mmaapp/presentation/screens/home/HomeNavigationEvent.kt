package com.berkekucuk.mmaapp.presentation.screens.home

sealed interface HomeNavigationEvent {
    data class ToEventDetail(val eventId: String) : HomeNavigationEvent
}
