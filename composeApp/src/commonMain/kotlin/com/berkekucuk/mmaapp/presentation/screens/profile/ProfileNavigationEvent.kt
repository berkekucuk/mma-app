package com.berkekucuk.mmaapp.presentation.screens.profile

sealed interface ProfileNavigationEvent {
    data object Back : ProfileNavigationEvent
    data class ToFavoriteFighters(val userId: String) : ProfileNavigationEvent
}
