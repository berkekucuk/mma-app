package com.berkekucuk.mmaapp.presentation.screens.rankings

sealed interface RankingUiAction {
    data class OnWeightClassClicked(val weightClassId: String, val weightClassName: String) : RankingUiAction
    data object OnRefresh : RankingUiAction
}