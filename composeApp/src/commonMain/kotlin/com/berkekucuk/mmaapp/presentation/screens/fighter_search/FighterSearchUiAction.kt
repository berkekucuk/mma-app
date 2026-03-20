package com.berkekucuk.mmaapp.presentation.screens.fighter_search

sealed interface FighterSearchUiAction {
    data class OnQueryChanged(val query: String) : FighterSearchUiAction
    data object OnClearQuery : FighterSearchUiAction
    data class OnFighterClicked(val fighterId: String) : FighterSearchUiAction
    data object OnBackClicked : FighterSearchUiAction
}
