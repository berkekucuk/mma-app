package com.berkekucuk.mmaapp.presentation.screens.favorite_fighters

sealed interface FavoriteFightersUiAction {
    data object OnBackClicked : FavoriteFightersUiAction
    data class OnFighterClicked(val fighterId: String) : FavoriteFightersUiAction
}
