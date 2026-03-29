package com.berkekucuk.mmaapp.presentation.screens.rankings

sealed interface RankingUiAction {
    data class OnWeightClassClicked(val weightClassId: String) : RankingUiAction
    data object OnRefresh : RankingUiAction
}