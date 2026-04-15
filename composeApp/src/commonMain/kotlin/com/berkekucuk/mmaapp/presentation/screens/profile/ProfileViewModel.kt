package com.berkekucuk.mmaapp.presentation.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.Profile>()
    private val userId: String = route.userId
    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<ProfileNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeUser()
        syncUser(userId)
    }

    private fun observeUser() {
        viewModelScope.launch {
            userRepository.getUser(userId)
                .collect { user ->
                _state.update {
                    it.copy(
                        user = user,
                        isLoading = false,
                        isRefreshing = false,
                    )
                }
            }
        }
    }

    private fun syncUser(id: String, isRefreshing: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = isRefreshing) }
            userRepository.syncUser(id)
                .onSuccess { _state.update { it.copy(isRefreshing = false) } }
                .onFailure { _state.update { it.copy(isRefreshing = false) } }
        }
    }

    fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.OnBackClicked -> navigateTo(ProfileNavigationEvent.Back)
            is ProfileUiAction.OnEditClicked -> navigateTo(ProfileNavigationEvent.ToEdit)
            is ProfileUiAction.OnSignOutClicked -> Unit
            is ProfileUiAction.OnRefresh -> syncUser(id = userId, isRefreshing = true)
        }
    }

    private fun navigateTo(event: ProfileNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}