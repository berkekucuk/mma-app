package com.berkekucuk.mmaapp.presentation.screens.profile_edit

import com.berkekucuk.mmaapp.core.utils.AppError

data class ProfileEditUiState(
    val fullName: String = "",
    val username: String = "",
    val originalFullName: String = "",
    val originalUsername: String = "",
    val isSaving: Boolean = false,
    val error: AppError? = null
)
