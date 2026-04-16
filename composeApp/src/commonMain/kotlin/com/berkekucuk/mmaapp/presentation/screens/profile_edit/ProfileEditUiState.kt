package com.berkekucuk.mmaapp.presentation.screens.profile_edit

data class ProfileEditUiState(
    val fullName: String = "",
    val username: String = "",
    val originalFullName: String = "",
    val originalUsername: String = "",
    val isSaving: Boolean = false,
    val error: ProfileEditError? = null
)

enum class ProfileEditError {
    NETWORK_ERROR,
    USERNAME_TAKEN,
    EMPTY_USERNAME,
    INVALID_USERNAME,
    USERNAME_TOO_SHORT,
    USERNAME_TOO_LONG,
    EMPTY_FULLNAME,
    FULLNAME_TOO_SHORT,
    FULLNAME_TOO_LONG,
    UNKNOWN_ERROR,
}


