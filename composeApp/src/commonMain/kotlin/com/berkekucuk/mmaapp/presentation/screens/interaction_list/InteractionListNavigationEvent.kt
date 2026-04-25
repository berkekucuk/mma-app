package com.berkekucuk.mmaapp.presentation.screens.interaction_list

sealed interface InteractionListNavigationEvent {
    data object Back : InteractionListNavigationEvent
    data class ToAddFighter(val userId: String) : InteractionListNavigationEvent
    data class ToFighterDetail(val fighterId: String) : InteractionListNavigationEvent
}
