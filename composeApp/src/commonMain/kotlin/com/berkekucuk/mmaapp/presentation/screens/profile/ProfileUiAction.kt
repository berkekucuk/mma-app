package com.berkekucuk.mmaapp.presentation.screens.profile

sealed interface ProfileUiAction {
    data object OnBackClicked : ProfileUiAction
    data object OnRefresh : ProfileUiAction
    data object OnFavoriteFightersClicked : ProfileUiAction
}
