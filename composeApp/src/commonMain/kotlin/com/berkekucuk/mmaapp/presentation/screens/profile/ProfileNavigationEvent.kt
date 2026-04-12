package com.berkekucuk.mmaapp.presentation.screens.profile

sealed interface ProfileNavigationEvent {
    data object Back : ProfileNavigationEvent
    data object ToEdit : ProfileNavigationEvent
}
