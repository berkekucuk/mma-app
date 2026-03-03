package com.berkekucuk.mmaapp.presentation.screens.rankings

sealed interface RankingNavigationEvent {
    data class ToFighterDetail(val fighterId: String) : RankingNavigationEvent
}
