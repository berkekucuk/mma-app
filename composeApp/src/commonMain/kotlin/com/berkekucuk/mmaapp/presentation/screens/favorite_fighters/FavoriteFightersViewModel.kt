package com.berkekucuk.mmaapp.presentation.screens.favorite_fighters

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

class FavoriteFightersViewModel(
    private val userRepository: UserRepository,
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
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            userRepository.getUser(userId)
                .collect { user ->
                _state.update { it.copy(fighters = user?.favoriteFighters ?: emptyList()) }
            }
        }
    }

    fun onAction(action: FavoriteFightersUiAction) {
        when (action) {
            is FavoriteFightersUiAction.OnBackClicked -> navigateTo(FavoriteFightersNavigationEvent.Back)
            is FavoriteFightersUiAction.OnFighterClicked -> navigateTo(FavoriteFightersNavigationEvent.ToFighterDetail(action.fighterId))
        }
    }

    private fun navigateTo(event: FavoriteFightersNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}
