package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.ProfileRepository
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.domain.repository.FighterRepository
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.domain.model.Fighter
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FightDetailViewModel(
    private val eventRepository: EventRepository,
    private val fighterRepository: FighterRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.FightDetail>()
    private val eventId: String = route.eventId
    private val fightId: String = route.fightId
    private val fighterId: String? = route.fighterId
    val cameFromEvent: Boolean = fighterId == null
    private val _state = MutableStateFlow(FightDetailUiState())
    val state = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<FightDetailNavigationEvent>()
    val navigation = _navigation.asSharedFlow()
    private var notificationObserverJob: Job? = null

    init {
        if (fighterId != null) {
            observeFightFromFighter()
        } else {
            observeFightFromEvent()
        }
    }

    private fun observeFightFromEvent() {
        viewModelScope.launch {
            eventRepository.getEventById(eventId)
                .collect { event ->
                    val fight = event.fights.find { it.fightId == fightId }

                    _state.update {
                        it.copy(
                            fight = fight,
                            eventDate = event.datetimeUtc,
                            isLoading = false,
                            eventName = event.name
                        )
                    }
                    if (fight != null) {
                        syncFighters(fight)
                        observeNotificationStatus(fight.fightId)
                    }
                }
        }
    }

    private fun observeFightFromFighter() {
        viewModelScope.launch {
            fighterRepository.getFighterById(fighterId!!)
                .collect { fighter ->
                    val fight = fighter.fights.find { it.fightId == fightId }

                    _state.update {
                        it.copy(
                            fight = fight,
                            eventName = fight?.eventName,
                            eventDate = fight?.eventDate,
                            isLoading = false,
                        )
                    }
                    if (fight != null) {
                        syncFighters(fight, knownFighterId = fighterId)
                        observeNotificationStatus(fight.fightId)
                    }
                }
        }
    }

    private fun observeNotificationStatus(fightId: String) {
        if (notificationObserverJob != null) return
        notificationObserverJob = viewModelScope.launch {
            val authState = authRepository.authState.first { it !is AuthState.Loading }
            if (authState is AuthState.Authenticated) {
                profileRepository.observeFightNotificationStatus(fightId, authState.userId)
                    .collect { isEnabled -> _state.update { it.copy(isNotificationEnabled = isEnabled) } }
            }
        }
        viewModelScope.launch {
            val authState = authRepository.authState.first { it !is AuthState.Loading }
            if (authState is AuthState.Authenticated) {
                profileRepository.syncFightNotificationStatus(fightId, authState.userId)
            }
        }
    }

    private fun syncFighters(fight: Fight, knownFighterId: String? = null) {
        val redId = fight.redCorner?.fighter?.fighterId ?: return
        val blueId = fight.blueCorner?.fighter?.fighterId ?: return
        _state.update { it.copy(error = null) }
        loadFighter(redId, sync = redId != knownFighterId) { fighter ->
            _state.update { it.copy(redFighter = fighter) }
        }
        loadFighter(blueId, sync = blueId != knownFighterId) {
            fighter -> _state.update { it.copy(blueFighter = fighter) }
        }
    }

    private fun loadFighter(fighterId: String, sync: Boolean, onUpdate: (Fighter) -> Unit) {
        viewModelScope.launch {
            if (sync) {
                fighterRepository.syncFighter(fighterId)
                    .onFailure { e ->
                    val errorType = when (e) {
                        is PostgrestRestException -> FightDetailError.UNKNOWN_ERROR
                        else -> FightDetailError.NETWORK_ERROR
                    }
                    _state.update { it.copy(error = errorType) }
                }
            }
            fighterRepository.getFighterById(fighterId).collect(onUpdate)
        }
    }

    fun onAction(action: FightDetailUiAction) {
        when (action) {
            is FightDetailUiAction.OnFighterClicked -> {
                if (action.fighterId == fighterId) {
                    navigateTo(FightDetailNavigationEvent.Back)
                } else {
                    navigateTo(FightDetailNavigationEvent.ToFighterDetail(action.fighterId))
                }
            }
            is FightDetailUiAction.OnBackClicked -> navigateTo(FightDetailNavigationEvent.Back)
            is FightDetailUiAction.OnRefresh -> onRefresh()
            is FightDetailUiAction.OnEventClicked -> navigateTo(FightDetailNavigationEvent.ToEventDetail(action.eventId))
            is FightDetailUiAction.OnNotificationClicked -> onNotificationClicked()
            is FightDetailUiAction.OnErrorShown -> _state.update { it.copy(error = null) }
        }
    }

    private fun onNotificationClicked() {
        viewModelScope.launch {
            val authState = authRepository.authState.first { it !is AuthState.Loading }
            if (authState !is AuthState.Authenticated) {
                _state.update { it.copy(error = FightDetailError.NOT_AUTHENTICATED) }
                return@launch
            }
            val fightId = _state.value.fight?.fightId ?: return@launch
            val isNotificationEnabled = _state.value.isNotificationEnabled
            val result = if (isNotificationEnabled) {
                profileRepository.removeFightNotification(fightId, authState.userId)
            } else {
                profileRepository.addFightNotification(fightId, authState.userId)
            }
            result.onFailure {
                _state.update { it.copy(error = FightDetailError.NETWORK_ERROR) }
            }
        }
    }

    private fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }
            val result = if (fighterId != null) {
                fighterRepository.syncFighter(fighterId)
            } else {
                eventRepository.syncEventById(eventId = eventId)
            }
            result.onSuccess {
                _state.value.fight?.let { currentFight ->
                    syncFighters(currentFight, knownFighterId = fighterId)
                }
                _state.update { it.copy(isRefreshing = false) }
            }.onFailure { e ->
                val errorType = when (e) {
                    is PostgrestRestException -> FightDetailError.UNKNOWN_ERROR
                    else -> FightDetailError.NETWORK_ERROR
                }
                _state.update { it.copy(isRefreshing = false, error = errorType) }
            }
        }
    }

    private fun navigateTo(event: FightDetailNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}