package com.berkekucuk.mmaapp.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.model.Profile
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<ProfileNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeAuthAndProfile()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeAuthAndProfile() {
        viewModelScope.launch {
            authRepository.authState
                .onEach { _state.update { state -> state.copy(authState = it) } }
                .flatMapLatest { authState -> resolveProfileFlow(authState) }
                .collect { profile ->
                    _state.update { it.copy(profile = profile, isLoading = false) }
                }
        }
    }

    private fun resolveProfileFlow(authState: AuthState): Flow<Profile?> {
        return if (authState is AuthState.Authenticated) {
            syncProfile(authState.userId)
            profileRepository.getProfile(authState.userId)
        } else {
            flowOf(null)
        }
    }

    private fun syncProfile(id: String, isRefreshing: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = isRefreshing) }
            profileRepository.syncProfile(id)
                .onSuccess { _state.update { it.copy(isRefreshing = false) } }
                .onFailure { _state.update { it.copy(isRefreshing = false) } }
        }
    }

    fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.OnEditClicked -> navigateTo(ProfileNavigationEvent.ToEdit)
            is ProfileUiAction.OnSignOutClicked -> {
                viewModelScope.launch {
                    authRepository.signOut()
                }
            }
            is ProfileUiAction.OnRefresh -> {
                val currentAuth = _state.value.authState
                if (currentAuth is AuthState.Authenticated) {
                    syncProfile(id = currentAuth.userId, isRefreshing = true)
                }
            }
        }
    }

    private fun navigateTo(event: ProfileNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}