package com.berkekucuk.mmaapp.presentation.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.repository.UserRepository
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardUiState())
    val state = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<LeaderboardNavigationEvent>()
    val navigation = _navigation.asSharedFlow()
    private var syncJob: Job? = null

    init {
        observeLeaderboard()
        syncLeaderboard()
    }

    private fun observeLeaderboard() {
        viewModelScope.launch {
            userRepository.getUsers(15)
                .collect { users ->
                _state.update { it.copy(leaderboard = users) }
            }
        }
    }

    private fun syncLeaderboard(isRefreshing: Boolean = false) {
        if (syncJob?.isActive == true) return

        syncJob = viewModelScope.launch {
            _state.update { it.copy(isRefreshing = isRefreshing, error = null) }

            userRepository.syncUsers(15)
                .onSuccess {
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onFailure { e ->
                    val errorType = when (e) {
                         is PostgrestRestException -> LeaderboardError.UNKNOWN_ERROR
                         else -> LeaderboardError.NETWORK_ERROR
                    }
                    _state.update { it.copy(isLoading = false, isRefreshing = false, error = errorType) }
                }

            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onAction(action: LeaderboardUiAction) {
        when (action) {
            LeaderboardUiAction.OnBackClicked -> navigateTo(LeaderboardNavigationEvent.Back)
            is LeaderboardUiAction.OnUserClicked -> navigateTo(LeaderboardNavigationEvent.ToUserProfile(action.userId))
            LeaderboardUiAction.OnRefresh -> syncLeaderboard(isRefreshing = true)
        }
    }

    private fun navigateTo(event: LeaderboardNavigationEvent) {
        viewModelScope.launch { _navigation.emit(event) }
    }
}
