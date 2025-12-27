package com.berkekucuk.mmaapp.presentation.screens.event_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.app.Route
import com.berkekucuk.mmaapp.domain.enums.EventStatus
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.domain.repository.FightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val eventRepository: EventRepository,
    private val fightRepository: FightRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.EventDetail>()
    private val eventId: String = route.eventId

    private val _state = MutableStateFlow(EventDetailUiState())
    val state = _state.asStateFlow()

    init {
        observeEvent()
        observeFights()
        syncFights()
    }

    private fun observeEvent() {
        viewModelScope.launch {
            eventRepository.getEventById(eventId).collect { event ->
                event.let {
                    _state.update { it.copy(event = event) }
                }
            }
        }
    }

    private fun observeFights() {
        viewModelScope.launch {
            fightRepository.getFightsByEvent(eventId)
                .catch { error ->
                    println("Error observing fights: $error")
                    _state.update { it.copy(isLoading = false) }
                }
                .collect { fights ->
                    processAndSetFights(fights)
                }
        }
    }

    private fun syncFights() {
        viewModelScope.launch {
            fightRepository.syncFights(
                eventId = eventId,
                status = state.value.event?.status ?: EventStatus.UNKNOWN,
                forceRefresh = false
            ).onSuccess {
                _state.update { it.copy(isLoading = false) }
            }.onFailure {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun processAndSetFights(fights: List<Fight>) {
        val mainCard = fights.filter { fight ->
            fight.boutType.contains("Main Card", ignoreCase = true)
        }.sortedBy { it.fightOrder }

        val prelims = fights.filter { fight ->
            fight.boutType.contains("Prelim", ignoreCase = true)
        }.sortedBy { it.fightOrder }

        _state.update {
            it.copy(
                allFights = fights,
                mainCardFights = mainCard,
                prelimFights = prelims,
            )
        }
    }
}