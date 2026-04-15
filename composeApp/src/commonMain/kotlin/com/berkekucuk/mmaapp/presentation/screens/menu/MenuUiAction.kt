package com.berkekucuk.mmaapp.presentation.screens.menu

sealed interface MenuUiAction {
    data object OnOpenProfileClicked : MenuUiAction
    data object OnOpenProfileEditClicked : MenuUiAction
    data object OnSettingsClicked : MenuUiAction
    data object OnSignOutClicked : MenuUiAction
}
