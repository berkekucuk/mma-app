package com.berkekucuk.mmaapp.presentation.screens.event_detail

import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.model.Fight

data class EventDetailUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val allFights: List<Fight> = emptyList(),
    val event: Event? = null,
    val mainCardFights: List<Fight> = emptyList(),
    val prelimFights: List<Fight> = emptyList()
)