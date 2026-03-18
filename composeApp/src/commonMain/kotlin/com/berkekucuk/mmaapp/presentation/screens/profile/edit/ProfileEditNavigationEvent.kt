package com.berkekucuk.mmaapp.presentation.screens.profile.edit

sealed interface ProfileEditNavigationEvent {
    data object Back : ProfileEditNavigationEvent
}
