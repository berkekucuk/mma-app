package com.berkekucuk.mmaapp.presentation.screens.profile

sealed interface ProfileNavigationEvent {
    data object Back : ProfileNavigationEvent
    data class ToInteractionList(val userId: String, val type: String) : ProfileNavigationEvent
    data class ToFightDetail(val fightId: String) : ProfileNavigationEvent
}
