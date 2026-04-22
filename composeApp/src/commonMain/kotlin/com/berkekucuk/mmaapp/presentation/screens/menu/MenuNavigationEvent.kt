package com.berkekucuk.mmaapp.presentation.screens.menu

sealed interface MenuNavigationEvent {
    data class ToProfile(val userId: String) : MenuNavigationEvent
    data class ToProfileEdit(val userId: String) : MenuNavigationEvent
    data object ToSettings : MenuNavigationEvent
    data object ToLeaderboard : MenuNavigationEvent
}
