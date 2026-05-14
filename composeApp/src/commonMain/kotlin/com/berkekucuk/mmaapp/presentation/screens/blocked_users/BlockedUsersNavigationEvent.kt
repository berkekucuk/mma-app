package com.berkekucuk.mmaapp.presentation.screens.blocked_users

sealed interface BlockedUsersNavigationEvent {
    data object Back : BlockedUsersNavigationEvent
    data class ToUserProfile(val userId: String) : BlockedUsersNavigationEvent
}
