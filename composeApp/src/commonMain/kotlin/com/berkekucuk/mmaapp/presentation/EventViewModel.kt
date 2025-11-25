package com.berkekucuk.mmaapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import kotlin.time.ExperimentalTime

class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.getEvents()
                .onSuccess { events ->

                    val selectedEventId = findClosestEventId(events)

                    _uiState.update {
                        it.copy(
                            events = events,
                            selectedEventId = selectedEventId,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown error"
                        )
                    }
                }
        }
    }
    @OptIn(ExperimentalTime::class)
    private fun findClosestEventId(events: List<Event>): String? {
        if (events.isEmpty()) return null

        val now = Clock.System.now().toEpochMilliseconds()
        var closestEvent: Event? = null
        var minDiff = Long.MAX_VALUE

        for (event in events) {
            val eventTime = event.date.toEpochMilliseconds()

            val diff = kotlin.math.abs(eventTime - now)

            if (diff > minDiff) {
                break
            }

            minDiff = diff
            closestEvent = event
        }

        return closestEvent?.id
    }
}