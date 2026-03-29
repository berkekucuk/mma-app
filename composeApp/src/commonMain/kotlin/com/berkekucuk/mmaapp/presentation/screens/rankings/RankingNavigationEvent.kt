package com.berkekucuk.mmaapp.presentation.screens.rankings

sealed interface RankingNavigationEvent {
    data class ToRankingDetail(val weightClassId: String) : RankingNavigationEvent
}
