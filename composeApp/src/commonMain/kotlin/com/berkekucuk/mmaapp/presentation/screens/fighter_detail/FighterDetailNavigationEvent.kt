package com.berkekucuk.mmaapp.presentation.screens.fighter_detail

sealed interface FighterDetailNavigationEvent {
    data class ToFightDetail(val eventId: String, val fightId: String, val fighterId: String) : FighterDetailNavigationEvent
    data object Back : FighterDetailNavigationEvent
}
