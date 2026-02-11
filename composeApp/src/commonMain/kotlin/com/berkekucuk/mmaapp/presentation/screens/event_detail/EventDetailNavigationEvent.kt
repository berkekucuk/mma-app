package com.berkekucuk.mmaapp.presentation.screens.event_detail

sealed interface EventDetailNavigationEvent {
    data class ToFightDetail(val eventId: String, val fightId: String) : EventDetailNavigationEvent
    data object Back : EventDetailNavigationEvent
}
