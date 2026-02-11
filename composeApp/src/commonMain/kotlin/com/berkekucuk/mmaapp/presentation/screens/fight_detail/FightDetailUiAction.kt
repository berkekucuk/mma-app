package com.berkekucuk.mmaapp.presentation.screens.fight_detail

sealed class FightDetailUiAction {
    data class OnFighterClicked(val fighterId: String): FightDetailUiAction()
    data object OnBackClicked: FightDetailUiAction()
    data object OnRefresh: FightDetailUiAction()
}
