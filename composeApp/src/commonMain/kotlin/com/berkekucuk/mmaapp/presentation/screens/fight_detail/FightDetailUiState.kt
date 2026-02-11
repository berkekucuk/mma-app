package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import com.berkekucuk.mmaapp.domain.model.Fight

data class FightDetailUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val fight: Fight? = null
)
