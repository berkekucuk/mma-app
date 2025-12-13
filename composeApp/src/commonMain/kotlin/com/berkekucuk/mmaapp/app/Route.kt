package com.berkekucuk.mmaapp.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data class EventDetail(val eventId: String) : Route

    @Serializable
    data class FightDetail(val fightId: String) : Route

    @Serializable
    data class FighterDetail(val fighterId: String) : Route
}