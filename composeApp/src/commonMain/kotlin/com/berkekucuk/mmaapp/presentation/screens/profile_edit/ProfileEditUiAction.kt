package com.berkekucuk.mmaapp.presentation.screens.profile_edit

sealed interface ProfileEditUiAction {
    data class OnFullNameChanged(val value: String) : ProfileEditUiAction
    data class OnUsernameChanged(val value: String) : ProfileEditUiAction
    data object OnSaveClicked : ProfileEditUiAction
    data object OnBackClicked : ProfileEditUiAction
}
