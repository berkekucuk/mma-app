package com.berkekucuk.mmaapp.presentation.screens.blocked_users

import com.berkekucuk.mmaapp.core.utils.AppError
import com.berkekucuk.mmaapp.domain.model.User

data class BlockedUsersUiState(
    val blockedUsers: List<User> = emptyList(),
    val error: AppError? = null,
)
