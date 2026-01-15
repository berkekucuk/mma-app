package com.berkekucuk.mmaapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.core.utils.DateTimeProvider
import com.berkekucuk.mmaapp.domain.enums.EventStatus
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours

class HomeViewModel(
    private val eventRepository: EventRepository,
    private val dateTimeProvider: DateTimeProvider
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<HomeNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        val currentYear = dateTimeProvider.currentYear
        _state.update {
            it.copy(
                selectedYear = currentYear,
                availableYears = (1993..currentYear).toList().reversed(),
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
                    _state.update { it.copy(allEvents = events) }
                    recalculateLists()
                }
        }
    }

    private fun syncEvents() {
        viewModelScope.launch {
            eventRepository.syncEvents()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun recalculateLists() {
        viewModelScope.launch(Dispatchers.Default) {
            val currentYear = dateTimeProvider.currentYear
            val now = dateTimeProvider.now
            val selectedYear = _state.value.selectedYear ?: currentYear
            val allEvents = _state.value.allEvents

            val thresholdDate = now.minus(24.hours)

            val featuredEvent = allEvents
                .filter {
                    it.datetimeUtc != null && it.datetimeUtc >= thresholdDate
                }
                .sortedBy { it.datetimeUtc }
                .firstOrNull()

            val upcomingEvents = allEvents
                .filter { it.status == EventStatus.UPCOMING }
                .sortedBy { it.datetimeUtc }

            val completedEvents = allEvents
                .filter { it.status == EventStatus.COMPLETED }
                .filter { it.eventYear == selectedYear }
                .sortedByDescending { it.datetimeUtc }

            _state.update {
                it.copy(
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
            is HomeUiAction.OnEventClicked -> navigateTo(HomeNavigationEvent.ToEventDetail(action.eventId))
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
            val allEvents = _state.value.allEvents
            val newCompletedList = allEvents
                .filter { it.status == EventStatus.COMPLETED }
                .filter { it.eventYear == year }
                .sortedByDescending { it.datetimeUtc }

            _state.update {
                it.copy(
                    completedEvents = newCompletedList,
                    isYearLoading = newCompletedList.isEmpty()
                )
            }

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
            eventRepository.refreshEvents()
                .onSuccess {
                    _state.update { it.copy(isRefreshingFeaturedTab = false) }
                }
                .onFailure {
                    _state.update { it.copy(isRefreshingFeaturedTab = false) }
                }
        }
    }

    private fun onRefreshUpcomingTab() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshingUpcomingTab = true) }
            eventRepository.refreshEvents()
                .onSuccess {
                    _state.update { it.copy(isRefreshingUpcomingTab = false) }
                }
                .onFailure {
                    _state.update { it.copy(isRefreshingUpcomingTab = false) }
                }
        }
    }

    private fun onRefreshCompletedTab() {
        val selectedYear = _state.value.selectedYear ?: dateTimeProvider.currentYear
        viewModelScope.launch {
            _state.update { it.copy(isRefreshingCompletedTab = true) }
            eventRepository.getEventsByYear(selectedYear, forceRefresh = true)
                .onSuccess {
                    _state.update { it.copy(isRefreshingCompletedTab = false) }
                }
                .onFailure {
                    _state.update { it.copy(isRefreshingCompletedTab = false) }
                }
        }
    }

    private fun navigateTo(event: HomeNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}
