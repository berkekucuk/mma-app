package com.berkekucuk.mmaapp.presentation.screens.ranking_detail

sealed interface RankingDetailUiAction {
    data object OnBackClicked : RankingDetailUiAction
    data class OnFighterClicked(val fighterId: String) : RankingDetailUiAction
    data object OnRefresh : RankingDetailUiAction
}
