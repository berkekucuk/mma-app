package com.berkekucuk.mmaapp.presentation.screens.favorite_fighters

sealed interface FavoriteFightersNavigationEvent {
    data object Back : FavoriteFightersNavigationEvent
    data class ToAddFighter(val userId: String) : FavoriteFightersNavigationEvent
    data class ToFighterDetail(val fighterId: String) : FavoriteFightersNavigationEvent
}
