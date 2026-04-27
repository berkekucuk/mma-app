package com.berkekucuk.mmaapp.presentation.screens.profile

sealed interface ProfileUiAction {
    data object OnBackClicked : ProfileUiAction
    data object OnRefresh : ProfileUiAction
    data class OnInteractionListClicked(val type: String) : ProfileUiAction
    data class OnPredictionClicked(val fightId: String) : ProfileUiAction
    data object OnErrorDismissed : ProfileUiAction
}
