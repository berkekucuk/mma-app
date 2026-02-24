package com.berkekucuk.mmaapp.presentation.screens.rankings

sealed interface RankingsUiAction {
    data class OnToggleExpand(val weightClassId: String) : RankingsUiAction
}