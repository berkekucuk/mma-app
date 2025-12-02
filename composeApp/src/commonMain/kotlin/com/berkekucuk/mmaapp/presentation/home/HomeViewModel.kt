package com.berkekucuk.mmaapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.domain.repository.FightCardRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.time.Clock

class HomeViewModel(
    private val eventRepository: EventRepository,
    private val fightCardRepository: FightCardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<NavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    private var fetchFightsJob: Job? = null

    init {
        syncEventsFromRemote()
        observeEvents()
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnPageChanged -> onPageChanged(action.index)
            is HomeUiAction.OnFightClicked -> navigateToFightDetail(action.fightId)
        }
    }

    private fun syncEventsFromRemote() {
        viewModelScope.launch {
            eventRepository.syncEvents()
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {

            eventRepository.getEvents()
                .distinctUntilChanged()
                .collectLatest { events ->

                    if (events.isEmpty()) {
                        _state.update { it.copy(events = emptyList(), isLoading = false) }
                        return@collectLatest
                    }

                    val previousEventId =
                        _state.value.events.getOrNull(_state.value.selectedIndex)?.id

                    val newIndex =
                        if (_state.value.events.isEmpty())
                            findClosestEventIndex(events)
                        else _state.value.selectedIndex

                    val validIndex = newIndex.coerceIn(0, events.lastIndex)

                    _state.update {
                        it.copy(
                            events = events,
                            selectedIndex = validIndex
                        )
                    }

                    // --- LOAD FIGHTS IF EVENT CHANGED ---
                    val selectedEvent = events.getOrNull(validIndex)
                    if (selectedEvent != null && selectedEvent.id != previousEventId) {
                        loadFightsForEvent(selectedEvent.id, selectedEvent.status.toString())
                    }
                }
        }
    }

    private fun onPageChanged(index: Int) {
        val event = _state.value.events.getOrNull(index) ?: return

        _state.update { it.copy(selectedIndex = index) }

        loadFightsForEvent(event.id, event.status.toString())
    }

    private fun loadFightsForEvent(eventId: String, status: String) {

        fetchFightsJob?.cancel()

        fetchFightsJob = viewModelScope.launch {

            _state.update { it.copy(isLoading = true) }

            // 1. DB STREAM
            launch {
                fightCardRepository.getFightsByEvent(eventId)
                    .collectLatest { fights ->
                        _state.update {
                            it.copy(
                                fights = fights,
                                isLoading = false
                            )
                        }
                    }
            }

            launch {
                val result = fightCardRepository.syncFightsByEvent(eventId, status)
                result.onFailure { e ->
                    _state.update {
                        it.copy(errorMessage = e.message, isLoading = false)
                    }
                }
            }
        }
    }

    private fun findClosestEventIndex(events: List<Event>): Int {
        val now = Clock.System.now().toEpochMilliseconds()

        return events.indices.minByOrNull { index ->
            abs(events[index].date.toEpochMilliseconds() - now)
        } ?: 0
    }

    private fun navigateToFightDetail(fightId: String) {
        viewModelScope.launch {
            _navigation.emit(NavigationEvent.ToFightDetail(fightId))
        }
    }
}


