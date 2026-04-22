package com.berkekucuk.mmaapp.presentation.screens.menu

import com.berkekucuk.mmaapp.domain.model.AuthState

data class MenuUiState(
    val authState: AuthState = AuthState.Loading,
    val userId: String? = null,
    val avatarUrl: String? = null,
    val name: String? = null,
    val username: String? = null,
)
