package com.berkekucuk.mmaapp.presentation.screens.blocked_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.core.utils.AppErrorMapper
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlockedUsersViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BlockedUsersUiState())
    val state = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<BlockedUsersNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeBlockedUsers()
    }

    private fun observeBlockedUsers() {
        viewModelScope.launch {
            val currentUserId = authRepository.getAuthenticatedUserId() ?: ""
            userRepository.getBlockedUsers(currentUserId)
                .collect { users ->
                    _state.update { it.copy(blockedUsers = users) }
                }
        }
    }

    private fun unblockUser(userId: String) {
        viewModelScope.launch {
            val currentUserId = authRepository.getAuthenticatedUserId() ?: return@launch
            userRepository.unblockUser(currentUserId, userId)
                .onFailure { e ->
                    _state.update { it.copy(error = AppErrorMapper.map(e)) }
                }
        }
    }

    fun onAction(action: BlockedUsersUiAction) {
        when (action) {
            BlockedUsersUiAction.OnBackClicked -> navigateTo(BlockedUsersNavigationEvent.Back)
            is BlockedUsersUiAction.OnUserClicked -> navigateTo(BlockedUsersNavigationEvent.ToUserProfile(action.userId))
            is BlockedUsersUiAction.OnUnblockClicked -> unblockUser(action.userId)
            BlockedUsersUiAction.OnErrorShown -> _state.update { it.copy(error = null) }
        }
    }

    private fun navigateTo(event: BlockedUsersNavigationEvent) {
        viewModelScope.launch { _navigation.emit(event) }
    }
}
