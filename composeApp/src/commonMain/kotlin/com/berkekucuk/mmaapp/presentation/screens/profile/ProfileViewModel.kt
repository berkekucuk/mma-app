package com.berkekucuk.mmaapp.presentation.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.Profile>()
    private val userId: String = route.userId
    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<ProfileNavigationEvent>()
    val navigation = _navigation.asSharedFlow()
    private var syncJob: Job? = null

    init {
        observeUser()
        syncUser()
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

    private fun syncUser(isRefreshing: Boolean = false) {
        if (syncJob?.isActive == true) return

        syncJob = viewModelScope.launch {
            _state.update { it.copy(isRefreshing = isRefreshing) }

            userRepository.syncUser(userId)

            val currentUserId = getAuthenticatedUserId()
            val isOwner = currentUserId == userId
            if (isOwner) {
                userRepository.syncFightNotifications(userId)
            }
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun getAuthenticatedUserId(): String? {
        val authState = authRepository.authState.first { it !is AuthState.Loading }
        return if (authState is AuthState.Authenticated) authState.userId else null
    }

    fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.OnBackClicked -> navigateTo(ProfileNavigationEvent.Back)
            is ProfileUiAction.OnRefresh -> syncUser(isRefreshing = true)
            is ProfileUiAction.OnFavoriteFightersClicked -> navigateTo(ProfileNavigationEvent.ToFavoriteFighters(userId))
        }
    }

    private fun navigateTo(event: ProfileNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}