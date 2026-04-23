package com.berkekucuk.mmaapp.presentation.screens.profile

import com.berkekucuk.mmaapp.domain.model.Prediction
import com.berkekucuk.mmaapp.domain.model.User

data class ProfileUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val user: User? = null,
    val predictions: List<Prediction> = emptyList()
)
