package com.berkekucuk.mmaapp.presentation.screens.leaderboard

import com.berkekucuk.mmaapp.domain.model.User

data class LeaderboardUiState(
    val isLoading: Boolean = true,
    val leaderboard: List<User> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: LeaderboardError? = null,
)

enum class LeaderboardError {
    NETWORK_ERROR,
    UNKNOWN_ERROR,
}
