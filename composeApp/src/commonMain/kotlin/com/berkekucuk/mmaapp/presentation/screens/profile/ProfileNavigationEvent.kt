package com.berkekucuk.mmaapp.presentation.screens.profile

sealed interface ProfileNavigationEvent {
    data object ToEdit : ProfileNavigationEvent
}
