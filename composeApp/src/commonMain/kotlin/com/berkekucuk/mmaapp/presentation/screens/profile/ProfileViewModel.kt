package com.berkekucuk.mmaapp.presentation.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.core.utils.AppErrorMapper
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.InteractionRepository
import com.berkekucuk.mmaapp.domain.repository.NotificationRepository
import com.berkekucuk.mmaapp.domain.repository.PredictionRepository
import com.berkekucuk.mmaapp.domain.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository,
    private val predictionRepository: PredictionRepository,
    private val interactionRepository: InteractionRepository,
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
        observeProfile()
        syncProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            userRepository.getUserProfile(userId)
                .collect { profile ->
                _state.update { it.copy(profile = profile, isLoading = false) }
            }
        }
    }

    private fun syncProfile(isRefreshing: Boolean = false) {
        if (syncJob?.isActive == true) return

        syncJob = viewModelScope.launch {
            _state.update { it.copy(isRefreshing = isRefreshing, error = null) }

            val userResult = userRepository.syncUser(userId)
            val interactionResult = interactionRepository.syncInteractions(userId)
            val predictionResult = predictionRepository.syncPredictions(userId)

            val currentUserId = authRepository.getAuthenticatedUserId()
            val notificationResult = if (currentUserId == userId) {
                notificationRepository.syncFightNotifications(userId)
            } else Result.success(Unit)

            val firstError = userResult.exceptionOrNull()
                ?: predictionResult.exceptionOrNull()
                ?: interactionResult.exceptionOrNull()
                ?: notificationResult.exceptionOrNull()

            if (firstError != null) {
                _state.update { it.copy(error = AppErrorMapper.map(firstError)) }
            }

            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.OnBackClicked -> navigateTo(ProfileNavigationEvent.Back)
            is ProfileUiAction.OnRefresh -> syncProfile(isRefreshing = true)
            is ProfileUiAction.OnInteractionListClicked -> navigateTo(ProfileNavigationEvent.ToInteractionList(userId, action.type))
            is ProfileUiAction.OnPredictionClicked -> navigateTo(ProfileNavigationEvent.ToFightDetail(action.fightId))
            is ProfileUiAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun navigateTo(event: ProfileNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}