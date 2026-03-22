package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.domain.repository.FighterRepository
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.domain.model.Fighter
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FightDetailViewModel(
    private val eventRepository: EventRepository,
    private val fighterRepository: FighterRepository,
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
                        loadFighterProfiles(fight)
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
                            eventDate = fight?.eventDate,
                            isLoading = false,
                            eventName = fight?.eventName
                        )
                    }
                    if (fight != null) {
                        loadFighterProfiles(fight)
                    }
                }
        }
    }

    private fun loadFighterProfiles(fight: Fight) {
        val redId = fight.redCorner?.fighter?.fighterId ?: return
        val blueId = fight.blueCorner?.fighter?.fighterId ?: return
        _state.update { it.copy(error = null) }
        syncFighterProfile(redId) { fighter -> _state.update { it.copy(redFighter = fighter) } }
        syncFighterProfile(blueId) { fighter -> _state.update { it.copy(blueFighter = fighter) } }
    }

    private fun syncFighterProfile(fighterId: String, onUpdate: (Fighter) -> Unit) {
        viewModelScope.launch {
            fighterRepository.syncFighter(fighterId)
                .onFailure { e ->
                    val errorType = when (e) {
                        is PostgrestRestException -> FightDetailError.UNKNOWN_ERROR
                        else -> FightDetailError.NETWORK_ERROR
                    }
                    _state.update { it.copy(error = errorType) }
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
            is FightDetailUiAction.OnRetry -> {
                val fight = _state.value.fight ?: return
                loadFighterProfiles(fight)
            }
            is FightDetailUiAction.OnEventClicked -> navigateTo(FightDetailNavigationEvent.ToEventDetail(action.eventId))
        }
    }

    private fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            if (fighterId != null) {
                fighterRepository.syncFighter(fighterId)
            } else {
                eventRepository.refreshEventById(eventId = eventId)
            }.onSuccess {
                _state.update { it.copy(isRefreshing = false) }
            }.onFailure {
                _state.update { it.copy(isRefreshing = false) }
            }
        }
    }

    private fun navigateTo(event: FightDetailNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}