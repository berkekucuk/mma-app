package com.berkekucuk.mmaapp.presentation.screens.event_detail

import com.berkekucuk.mmaapp.domain.model.Fight

data class EventDetailUiState(
    val isLoading: Boolean = false,
    val allFights: List<Fight> = emptyList(),
    val mainCardFights: List<Fight> = emptyList(),
    val prelimFights: List<Fight> = emptyList(),
)