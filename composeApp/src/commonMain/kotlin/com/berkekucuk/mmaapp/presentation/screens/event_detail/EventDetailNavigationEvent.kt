package com.berkekucuk.mmaapp.presentation.screens.event_detail

sealed interface EventDetailNavigationEvent {
    data class ToFightDetail(val fightId: String) : EventDetailNavigationEvent
    data object Back : EventDetailNavigationEvent
}
