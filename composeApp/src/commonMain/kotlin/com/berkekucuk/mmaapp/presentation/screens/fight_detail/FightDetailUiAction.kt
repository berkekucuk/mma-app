package com.berkekucuk.mmaapp.presentation.screens.fight_detail

sealed interface FightDetailUiAction {
    data class OnFighterClicked(val fighterId: String): FightDetailUiAction
    data object OnBackClicked: FightDetailUiAction
    data object OnRefresh: FightDetailUiAction
}
