package com.berkekucuk.mmaapp.presentation.screens.profile

import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.model.Profile

data class ProfileUiState(
    val authState: AuthState = AuthState.Loading,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val profile: Profile? = null,
)
