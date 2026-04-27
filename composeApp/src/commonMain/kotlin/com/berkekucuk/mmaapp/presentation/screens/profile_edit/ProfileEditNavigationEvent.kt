package com.berkekucuk.mmaapp.presentation.screens.profile_edit

sealed interface ProfileEditNavigationEvent {
    data object Back : ProfileEditNavigationEvent
    data object AccountDeleted : ProfileEditNavigationEvent
}
