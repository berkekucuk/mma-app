package com.berkekucuk.mmaapp.presentation.screens.fighter_detail

sealed interface FighterDetailUiAction {
    data class OnFightClicked(val eventId: String, val fightId: String) : FighterDetailUiAction
    data object OnBackClicked : FighterDetailUiAction
    data object OnRefresh : FighterDetailUiAction
}
