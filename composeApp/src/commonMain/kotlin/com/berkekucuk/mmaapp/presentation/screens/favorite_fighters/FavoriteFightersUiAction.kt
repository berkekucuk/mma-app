package com.berkekucuk.mmaapp.presentation.screens.favorite_fighters

sealed interface FavoriteFightersUiAction {
    data object OnBackClicked : FavoriteFightersUiAction
    data object OnAddFighterClicked : FavoriteFightersUiAction
    data class OnFighterClicked(val fighterId: String) : FavoriteFightersUiAction
    data class OnRemoveFighterClicked(val fighterId: String) : FavoriteFightersUiAction
    data object OnRefresh : FavoriteFightersUiAction
}