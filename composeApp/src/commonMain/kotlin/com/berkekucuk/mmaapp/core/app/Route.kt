package com.berkekucuk.mmaapp.core.app

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object MainGraph : Route
    @Serializable data object Home : Route
    @Serializable data object Rankings : Route
    @Serializable data object Menu : Route
    @Serializable data class Profile(val userId: String) : Route
    @Serializable data class ProfileEdit(val userId: String) : Route
    @Serializable data class EventDetail(val eventId: String, val fromFightDetail: Boolean = false) : Route
    @Serializable data class FightDetail(val eventId: String, val fightId: String, val fighterId: String? = null) : Route
    @Serializable data class FighterDetail(val fighterId: String) : Route
    @Serializable data class RankingDetail(val weightClassId: String) : Route
    @Serializable data class FavoriteFighters(val userId: String) : Route
    @Serializable data object FighterSearch : Route
    @Serializable data object Settings : Route
}
