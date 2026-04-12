package com.berkekucuk.mmaapp.presentation.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<MenuNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.authState.collect { authState ->
                _state.update {
                    it.copy(
                        authState = authState,
                        userId = if (authState is AuthState.Authenticated) {
                            authState.userId
                        } else {
                            null
                        }
                    )
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
