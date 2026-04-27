package com.berkekucuk.mmaapp.presentation.screens.interaction_list

sealed interface InteractionListUiAction {
    data object OnBackClicked : InteractionListUiAction
    data object OnAddFighterClicked : InteractionListUiAction
    data class OnFighterClicked(val fighterId: String) : InteractionListUiAction
    data class OnRemoveFighterClicked(val fighterId: String) : InteractionListUiAction
    data object OnConfirmRemove : InteractionListUiAction
    data object OnDismissRemove : InteractionListUiAction
    data object OnRefresh : InteractionListUiAction
    data object OnErrorDismissed : InteractionListUiAction
    data object OnDismissLimitAlert : InteractionListUiAction
}