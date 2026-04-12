package com.berkekucuk.mmaapp.presentation.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<MenuNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeAuthState()
        observeProfile()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.authState
                .collect { authState ->
                _state.update {
                    it.copy(
                        authState = authState,
                        userId = if (authState is AuthState.Authenticated) {
                            authState.userId
                        } else { null }
                    )
                }
            }
        }
    }

    private fun observeProfile() {
        viewModelScope.launch {
            authRepository.authState
                .map { it as? AuthState.Authenticated }
                .distinctUntilChangedBy { it?.userId }
                .collectLatest { authenticated ->
                    if (authenticated == null) {
                        _state.update { it.copy(avatarUrl = null) }
                        return@collectLatest
                    }

                    userRepository.syncUser(authenticated.userId)

                    userRepository.getUser(authenticated.userId)
                        .collect { user ->
                            _state.update { state ->
                                state.copy(avatarUrl = user?.avatarUrl)
                            }
                        }
                }
        }
    }

    fun onAction(action: MenuUiAction) {
        when (action) {
            MenuUiAction.OnOpenProfileClicked -> {
                _state.value.userId?.let { userId ->
                    navigateTo(MenuNavigationEvent.ToProfile(userId))
                }
            }
            MenuUiAction.OnOpenProfileEditClicked -> {
                _state.value.userId?.let { userId ->
                    navigateTo(MenuNavigationEvent.ToProfileEdit(userId))
                }
            }
            MenuUiAction.OnSignOutClicked -> {
                viewModelScope.launch {
                    authRepository.signOut()
                }
            }
        }
    }

    private fun navigateTo(event: MenuNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}
