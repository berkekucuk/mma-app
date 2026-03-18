package com.berkekucuk.mmaapp.presentation.screens.ranking_detail

sealed interface RankingDetailNavigationEvent {
    data object Back : RankingDetailNavigationEvent
    data class ToFighterDetail(val fighterId: String) : RankingDetailNavigationEvent
}
