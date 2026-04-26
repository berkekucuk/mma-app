package com.berkekucuk.mmaapp.presentation.screens.interaction_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.InteractionRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class InteractionListViewModel(
    private val interactionRepository: InteractionRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.InteractionList>()
    private val userId: String = route.userId
    private val interactionType: String = route.interactionType
    private val _state = MutableStateFlow(InteractionListUiState(type = interactionType))
    val state: StateFlow<InteractionListUiState> = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<InteractionListNavigationEvent>()
    val navigation = _navigation.asSharedFlow()
    private var syncJob: Job? = null

    init {
        observeInteractions()
        observeAuthState()
    }

    private fun observeInteractions() {
        viewModelScope.launch {
            interactionRepository.getInteractions(userId, interactionType)
                .collect { interactions ->
                    _state.update {
                        it.copy(
                            interactions = interactions
                        ) 
                    }
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

    fun onAction(action: InteractionListUiAction) {
        when (action) {
            is InteractionListUiAction.OnBackClicked -> navigateTo(InteractionListNavigationEvent.Back)
            is InteractionListUiAction.OnAddFighterClicked -> navigateTo(InteractionListNavigationEvent.ToAddFighter(interactionType))
            is InteractionListUiAction.OnFighterClicked -> navigateTo(InteractionListNavigationEvent.ToFighterDetail(action.fighterId))
            is InteractionListUiAction.OnRemoveFighterClicked -> _state.update { 
                it.copy(deletingFighterId = action.fighterId) 
            }
            is InteractionListUiAction.OnConfirmRemove -> {
                val fighterId = _state.value.deletingFighterId
                if (fighterId != null) {
                    removeInteraction(fighterId)
                }
                _state.update { it.copy(deletingFighterId = null) }
            }
            is InteractionListUiAction.OnDismissRemove -> _state.update { 
                it.copy(deletingFighterId = null) 
            }
            is InteractionListUiAction.OnRefresh -> syncInteractions()
        }
    }

    private fun syncInteractions() {
        if (syncJob?.isActive == true) return
        syncJob = viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            interactionRepository.syncInteractions(userId)
                .onSuccess {
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onFailure {
                    _state.update { it.copy(isRefreshing = false) }
                }
        }
    }

    private fun removeInteraction(fighterId: String) {
        viewModelScope.launch {
            _state.update { it.copy(error = null) }
            val interaction = _state.value.interactions.find { it.fighterId == fighterId }
            interaction?.let {
                interactionRepository.removeInteraction(it.id)
            }
        }
    }

    private fun navigateTo(event: InteractionListNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}
