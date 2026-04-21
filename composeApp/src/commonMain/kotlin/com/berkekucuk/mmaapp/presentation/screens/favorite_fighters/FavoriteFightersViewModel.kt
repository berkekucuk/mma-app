package com.berkekucuk.mmaapp.presentation.screens.favorite_fighters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteFightersViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.FavoriteFighters>()
    private val userId: String = route.userId
    private val _state = MutableStateFlow(FavoriteFightersUiState())
    val state: StateFlow<FavoriteFightersUiState> = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<FavoriteFightersNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeFavorites()
        observeAuthState()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            userRepository.getUser(userId)
                .collect { user ->
                _state.update { it.copy(fighters = user?.favoriteFighters ?: emptyList()) }
            }
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.authState
                .collect { authState ->
                val isOwner = authState is AuthState.Authenticated && authState.userId == userId
                _state.update { it.copy(isOwner = isOwner) }
            }
        }
    }

    fun onAction(action: FavoriteFightersUiAction) {
        when (action) {
            is FavoriteFightersUiAction.OnBackClicked -> navigateTo(FavoriteFightersNavigationEvent.Back)
            is FavoriteFightersUiAction.OnAddFighterClicked -> navigateTo(FavoriteFightersNavigationEvent.ToAddFighter)
            is FavoriteFightersUiAction.OnFighterClicked -> navigateTo(FavoriteFightersNavigationEvent.ToFighterDetail(action.fighterId))
            is FavoriteFightersUiAction.OnRemoveFighterClicked -> removeFavoriteFighter(action.fighterId)
            is FavoriteFightersUiAction.OnRefresh -> syncUser()
        }
    }

    private fun syncUser() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            userRepository.syncUser(userId)
                .onSuccess {
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onFailure {
                    _state.update { it.copy(isRefreshing = false) }
                }
        }
    }

    private fun removeFavoriteFighter(fighterId: String) {
        viewModelScope.launch {
            _state.update { it.copy(error = null) }
            userRepository.removeFavoriteFighter(userId, fighterId)
                .onSuccess {}
                .onFailure {
                    _state.update { it.copy(error = FavoriteFightersError.NETWORK_ERROR) }
                }
        }
    }

    private fun navigateTo(event: FavoriteFightersNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}
