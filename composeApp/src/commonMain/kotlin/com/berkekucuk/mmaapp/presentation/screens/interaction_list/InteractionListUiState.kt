package com.berkekucuk.mmaapp.presentation.screens.interaction_list

import com.berkekucuk.mmaapp.core.utils.AppError
import com.berkekucuk.mmaapp.domain.model.Interaction

data class InteractionListUiState(
    val interactions: List<Interaction> = emptyList(),
    val type: String = "",
    val isOwner: Boolean = false,
    val isRefreshing: Boolean = false,
    val deletingFighterId: String? = null,
    val showLimitAlert: Boolean = false,
    val error: AppError? = null,
)