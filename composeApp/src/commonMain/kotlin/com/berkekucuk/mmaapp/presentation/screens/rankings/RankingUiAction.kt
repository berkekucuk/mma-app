package com.berkekucuk.mmaapp.presentation.screens.rankings

sealed interface RankingUiAction {
    data class OnToggleExpand(val weightClassId: String) : RankingUiAction
    data class OnFighterClicked(val fighterId: String): RankingUiAction
    data object OnRefresh : RankingUiAction
}