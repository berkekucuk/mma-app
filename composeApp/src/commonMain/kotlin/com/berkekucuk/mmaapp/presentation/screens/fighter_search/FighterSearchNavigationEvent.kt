package com.berkekucuk.mmaapp.presentation.screens.fighter_search

sealed interface FighterSearchNavigationEvent {
    data class ToFighterDetail(val fighterId: String) : FighterSearchNavigationEvent
    data object Back : FighterSearchNavigationEvent
}
