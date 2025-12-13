package com.berkekucuk.mmaapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.core.time.DateTimeProvider
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.model.EventStatus
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs

class HomeViewModel(
    private val eventRepository: EventRepository,
    private val dateTimeProvider: DateTimeProvider
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<NavigationEvent>()
    val navigation = _navigation.asSharedFlow()
    private var allEvents: List<Event> = emptyList()

    init {
        val currentYear = dateTimeProvider.currentYear
        _state.update {
            it.copy(
                selectedYear = currentYear,
                availableYears = (1993..currentYear).toList().reversed(),
                isLoading = true
            )
        }

        observeEvents()
        syncEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            eventRepository.getEvents()
                .catch { error ->
                    println("Error observing events: $error")
                    _state.update { it.copy(isLoading = false) }
                }
                .collect { events ->
                    allEvents = events
                    recalculateLists()
                }
        }
    }

    private fun syncEvents() {
        viewModelScope.launch {
            if (allEvents.isEmpty()) {
                _state.update { it.copy(isLoading = true) }
            }

            eventRepository.syncEvents()
                .onFailure {
                    if (allEvents.isEmpty()) _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun recalculateLists() {
        viewModelScope.launch(Dispatchers.Default) {
            val currentState = _state.value
            val currentYear = dateTimeProvider.currentYear
            val selectedYear = currentState.selectedYear ?: currentYear
            val now = dateTimeProvider.now

            val featuredEvent = allEvents
                .filter { it.datetimeUtc != null }
                .minByOrNull { abs((it.datetimeUtc!! - now).inWholeMilliseconds) }

            val upcomingEvents = allEvents
                .filter { it.status == EventStatus.UPCOMING }
                .sortedBy { it.datetimeUtc }

            val completedEvents = allEvents
                .filter { it.status == EventStatus.COMPLETED }
                .filter { it.datetimeUtc?.toLocalDateTime(dateTimeProvider.timeZone)?.year == selectedYear }
                .sortedByDescending { it.datetimeUtc }

            _state.update {
                it.copy(
                    isLoading = false,
                    isYearLoading = false,
                    featuredEvent = featuredEvent,
                    upcomingEvents = upcomingEvents,
                    completedEvents = completedEvents,
                    selectedYear = selectedYear
                )
            }
        }
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnEventClicked -> navigateTo(NavigationEvent.ToEventDetail(action.eventId))
            is HomeUiAction.OnYearSelected -> onYearSelected(action.year)
            is HomeUiAction.OnRefreshFeaturedTab -> onRefreshFeaturedTab()
            is HomeUiAction.OnRefreshUpcomingTab -> onRefreshUpcomingTab()
            is HomeUiAction.OnRefreshCompletedTab -> onRefreshCompletedTab()
        }
    }

    private fun onYearSelected(year: Int) {
        if (_state.value.selectedYear == year) return

        _state.update {
            it.copy(
                selectedYear = year,
                isYearLoading = true
            )
        }

        viewModelScope.launch(Dispatchers.Default) {
            val newCompletedList = allEvents
                .filter { it.status == EventStatus.COMPLETED }
                .filter { it.datetimeUtc?.toLocalDateTime(dateTimeProvider.timeZone)?.year == year }
                .sortedByDescending { it.datetimeUtc }

            _state.update {
                it.copy(
                    completedEvents = newCompletedList,
                    isYearLoading = newCompletedList.isEmpty()
                )
            }
        }

        viewModelScope.launch {
            eventRepository.getEventsByYear(year)
                .onSuccess {
                    _state.update { it.copy(isYearLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isYearLoading = false) }
                }
        }
    }

    private fun onRefreshFeaturedTab() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshingFeaturedTab = true) }
            eventRepository.refreshFeaturedEventTab()
            _state.update { it.copy(isRefreshingFeaturedTab = false) }
        }
    }

    private fun onRefreshUpcomingTab() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshingUpcomingTab = true) }
            eventRepository.refreshUpcomingEventsTab()
            _state.update { it.copy(isRefreshingUpcomingTab = false) }
        }
    }

    private fun onRefreshCompletedTab() {
        val selectedYear = _state.value.selectedYear ?: dateTimeProvider.currentYear
        viewModelScope.launch {
            _state.update { it.copy(isRefreshingCompletedTab = true) }
            eventRepository.getEventsByYear(selectedYear, forceRefresh = true)
            _state.update { it.copy(isRefreshingCompletedTab = false) }
        }
    }

    private fun navigateTo(event: NavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}