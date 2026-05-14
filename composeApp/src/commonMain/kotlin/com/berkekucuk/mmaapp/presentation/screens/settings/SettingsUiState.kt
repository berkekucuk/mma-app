package com.berkekucuk.mmaapp.presentation.screens.settings

import com.berkekucuk.mmaapp.domain.model.AuthState

data class SettingsUiState(
    val authState: AuthState = AuthState.Unauthenticated
)
