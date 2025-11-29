package com.berkekucuk.mmaapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.domain.repository.FightCardRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.math.abs

class HomeViewModel(
    private val eventRepository: EventRepository,
    private val fightCardRepository: FightCardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private var fetchFightsJob: Job? = null

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            launch {
                eventRepository.getEvents()
                    .collect { events ->
                        val currentSelectedEvent = _uiState.value.selectedEvent
                        val targetSelectedEvent = currentSelectedEvent ?: findClosestEvent(events)

                        _uiState.update {
                            it.copy(
                                events = events,
                                selectedEvent = targetSelectedEvent,
                                isLoadingEvents = false,
                            )
                        }

                        val shouldLoadFights: Boolean = currentSelectedEvent != targetSelectedEvent || _uiState.value.fightCards.isEmpty()

                        if (shouldLoadFights && targetSelectedEvent != null) {
                            val eventId = targetSelectedEvent.id
                            val status = targetSelectedEvent.status.toString()
                            loadFightsForEvent(eventId, status)
                        }
                    }
            }

            launch {
                eventRepository.syncEvents()
            }
        }
    }

    fun onEventSelected(event: Event) {
        _uiState.update {
            it.copy(
                selectedEvent = event,
                fightCards = emptyList(),
                isLoadingFights = true
            )
        }
        val eventId = event.id
        val status = event.status.toString()
        loadFightsForEvent(eventId, status)
    }

    private fun loadFightsForEvent(eventId: String, status: String) {
        fetchFightsJob?.cancel()

        fetchFightsJob = viewModelScope.launch {
            launch {
                fightCardRepository.getFightsByEvent(eventId)
                    .collect { fightCards ->
                        _uiState.update {
                            it.copy(
                                fightCards = fightCards,
                                isLoadingFights = false
                            )
                        }
                    }
            }

            launch {
                val result = fightCardRepository.syncFightsByEvent(eventId, status)
                result.onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            }
        }
    }

    private fun findClosestEvent(events: List<Event>): Event? {
        if (events.isEmpty()) return null

        val now = Clock.System.now().toEpochMilliseconds()

        return events.minByOrNull { event ->
            val eventTime = event.date.toEpochMilliseconds()
            abs(eventTime - now)
        }
    }
}