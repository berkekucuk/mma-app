package com.berkekucuk.mmaapp.presentation.screens.blocked_users

sealed interface BlockedUsersUiAction {
    data object OnBackClicked : BlockedUsersUiAction
    data class OnUserClicked(val userId: String) : BlockedUsersUiAction
    data class OnUnblockClicked(val userId: String) : BlockedUsersUiAction
    data object OnErrorShown : BlockedUsersUiAction
}
