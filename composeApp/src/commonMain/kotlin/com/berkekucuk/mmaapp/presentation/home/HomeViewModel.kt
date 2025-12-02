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
        observeEvents()
        syncEventsFromRemote()
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnPageChanged -> onPageChanged(action.index)
            is HomeUiAction.OnFightClicked -> navigateToFightDetail(action.fightId)
        }
    }

    private fun onPageChanged(index: Int) {
        val event = _state.value.events.getOrNull(index) ?: return
        _state.update { it.copy(selectedIndex = index) }
        loadFightsForEvent(event.id, event.status.toString())
    }

    private fun navigateToFightDetail(fightId: String) {
        viewModelScope.launch {
            _navigation.emit(NavigationEvent.ToFightDetail(fightId))
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {

            eventRepository.getEvents()
                .distinctUntilChanged()
                .collectLatest { events ->
                    if (events.isEmpty()) return@collectLatest

                    val isFirstLoad = _state.value.initialIndex == null

                    if (isFirstLoad) {
                        val closestIndex = findClosestEventIndex(events)

                        _state.update {
                            it.copy(
                                events = events,
                                selectedIndex = closestIndex,
                                initialIndex = closestIndex
                            )
                        }

                        loadFightsForEvent(events[closestIndex].id, events[closestIndex].status.toString())
                    } else {
                        _state.update {
                            it.copy(events = events)
                        }
                    }
                }
        }
    }

    private fun syncEventsFromRemote() {
        viewModelScope.launch {
            eventRepository.syncEvents()
        }
    }

    private fun loadFightsForEvent(eventId: String, status: String) {

        fetchFightsJob?.cancel()

        fetchFightsJob = viewModelScope.launch {

            _state.update { it.copy(isFightsLoading = true) }

            launch {
                fightCardRepository.getFightsByEvent(eventId)
                    .collectLatest { fights ->
                        _state.update {
                            it.copy(
                                fights = fights,
                                isFightsLoading = false
                            )
                        }
                    }
            }

            launch {
                val result = fightCardRepository.syncFightsByEvent(eventId, status)
                result.onFailure { e ->
                    _state.update {
                        it.copy(errorMessage = e.message, isFightsLoading = false)
                    }
                }
            }
        }
    }

    private fun findClosestEventIndex(events: List<Event>): Int {
        if (events.isEmpty()) return 0

        events.reversed()

        val now = Clock.System.now().toEpochMilliseconds()
        var closestIndex = 0
        var minDiff = Long.MAX_VALUE

        for (i in events.indices) {
            val diff = abs(events[i].date.toEpochMilliseconds() - now)

            if (diff < minDiff) {
                minDiff = diff
                closestIndex = i
            } else {
                return closestIndex
            }
        }
        return closestIndex
    }
}


