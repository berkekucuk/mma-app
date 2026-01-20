package com.berkekucuk.mmaapp.presentation.screens.event_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.app.Route
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val eventRepository: EventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.EventDetail>()
    private val eventId: String = route.eventId
    private val _state = MutableStateFlow(EventDetailUiState())
    val state = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<EventDetailNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeEvent()
    }

    private fun observeEvent() {
        viewModelScope.launch {
            eventRepository.getEventById(eventId).collect { event ->
                _state.update {
                    it.copy(
                        event = event,
                        isLoading = false
                    )
                }
                processAndSetFights(event.fights)
            }
        }
    }

    private fun processAndSetFights(fights: List<Fight>) {
        val mainCard = fights.filter { fight ->
            fight.boutType.contains("Main Card", ignoreCase = true) ||
            fight.boutType.contains("Main Event", ignoreCase = true) ||
            fight.boutType.contains("Co-Main", ignoreCase = true)
        }.sortedByDescending { it.fightOrder }

        val prelims = fights.filter { fight ->
            fight.boutType.contains("Prelim", ignoreCase = true)
        }.sortedByDescending { it.fightOrder }

        _state.update {
            it.copy(
                allFights = fights,
                mainCardFights = mainCard,
                prelimFights = prelims,
            )
        }
    }

    fun onAction(action: EventDetailUiAction){
        when(action){
            is EventDetailUiAction.OnFightClicked -> navigateTo(EventDetailNavigationEvent.ToFightDetail(action.fightId))
            is EventDetailUiAction.OnBackClicked -> navigateTo(EventDetailNavigationEvent.Back)
            is EventDetailUiAction.OnRefresh -> onRefresh()
        }
    }

    private fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            eventRepository.refreshEventById(eventId = eventId)
                .onSuccess {
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onFailure {
                    _state.update { it.copy(isRefreshing = false) }
                }
        }
    }

    private fun navigateTo(event: EventDetailNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}
