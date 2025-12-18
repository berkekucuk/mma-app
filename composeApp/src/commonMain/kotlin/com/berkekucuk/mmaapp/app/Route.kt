package com.berkekucuk.mmaapp.app

import kotlinx.serialization.Serializable
sealed interface Route {
    @Serializable data object MainGraph : Route
    @Serializable data object Home : Route
    @Serializable data object Rankings : Route
    @Serializable data object Profile : Route
    @Serializable data class EventDetail(val eventId: String) : Route
    @Serializable data class FighterDetail(val fighterId: String) : Route
}