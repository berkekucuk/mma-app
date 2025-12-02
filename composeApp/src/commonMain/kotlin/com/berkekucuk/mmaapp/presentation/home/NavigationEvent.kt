package com.berkekucuk.mmaapp.presentation.home

sealed interface NavigationEvent {
    data class ToFightDetail(val fightId: String) : NavigationEvent
    data class ToFighterDetail(val fighterId: String) : NavigationEvent
    data object Back : NavigationEvent
}