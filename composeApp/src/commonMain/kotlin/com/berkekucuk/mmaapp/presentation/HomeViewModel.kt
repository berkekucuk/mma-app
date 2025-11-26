package com.berkekucuk.mmaapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class HomeViewModel(
    private val repository: EventRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadEvents()
    }
    private fun loadEvents() {
        viewModelScope.launch {
            repository.getEvents()
                .collect { events ->
                    val currentSelectedId = _uiState.value.selectedEventId
                    val targetSelectedId = currentSelectedId ?: findClosestEventId(events)

                    _uiState.update {
                        it.copy(
                            events = events,
                            selectedEventId = targetSelectedId,
                            isLoading = false,
                            error = null
                        )
                    }
                }

        }

        viewModelScope.launch {
            repository.syncEvents()
        }
    }
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

    fun onEventSelected(eventId: String) {
        _uiState.update { it.copy(selectedEventId = eventId) }
    }
}