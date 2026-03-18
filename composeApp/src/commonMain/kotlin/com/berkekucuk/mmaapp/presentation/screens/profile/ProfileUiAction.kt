package com.berkekucuk.mmaapp.presentation.screens.profile

sealed interface ProfileUiAction {
    data object OnEditClicked : ProfileUiAction
    data object OnSignOutClicked : ProfileUiAction
    data object OnRefresh : ProfileUiAction
}
