package com.berkekucuk.mmaapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.Instant

class HomeViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<NavigationEvent>()
    val navigation = _navigation.asSharedFlow()
    private var allEvents: List<Event> = emptyList()
    private val currentYear: Int = Clock.System.now().toLocalDateTime(TimeZone.UTC).year
    private val availableYears: List<Int> = (1993..currentYear).toList().reversed()

    init {
        initializeState()
        observeEvents()
        syncEvents()
    }

    private fun initializeState() {
        _state.update {
            it.copy(
                selectedYear = currentYear,
                availableYears = availableYears
            )
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            eventRepository.getEvents()
                .catch {
                    _state.update { it.copy(isLoading = false, isYearLoading = false) }
                }
                .collect { events ->
                    allEvents = events
                    recalculateAllState()
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
                    if (allEvents.isEmpty()) {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun recalculateAllState() {
        val currentEvents = allEvents
        val selectedYear = _state.value.selectedYear ?: currentYear

        viewModelScope.launch(Dispatchers.Default) {
            val now = Clock.System.now()

            val validEvents = currentEvents.filter {
                it.status != EventStatus.CANCELLED && it.status != EventStatus.UNKNOWN
            }

            val featuredEvent = findFeaturedEvent(validEvents, now)

            val upcomingEvents = validEvents
                .filter { it.status == EventStatus.UPCOMING }
                .sortedBy { it.datetimeUtc }

            val completedEvents = filterCompletedEvents(validEvents, selectedYear)

            withContext(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isYearLoading = false,
                        featuredEvent = featuredEvent,
                        upcomingEvents = upcomingEvents,
                        completedEvents = completedEvents
                    )
                }
            }
        }
    }

    private fun onYearSelected(year: Int) {
        if (_state.value.selectedYear == year) return

        _state.update { it.copy(selectedYear = year, isYearLoading = true) }

        viewModelScope.launch(Dispatchers.Default) {
            val newCompletedList = filterCompletedEvents(allEvents, year)

            withContext(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        completedEvents = newCompletedList,
                        isYearLoading = false
                    )
                }
            }
        }

        viewModelScope.launch {
            eventRepository.getEventsByYear(year)
                .onFailure {
                    _state.update { it.copy(isYearLoading = false) }
                }
        }
    }

    private fun filterCompletedEvents(events: List<Event>, year: Int): List<Event> {
        return events
            .filter { it.status == EventStatus.COMPLETED }
            .filter { event ->
                event.datetimeUtc?.toLocalDateTime(TimeZone.UTC)?.year == year
            }
            .sortedByDescending { it.datetimeUtc }
    }

    private fun findFeaturedEvent(events: List<Event>, now: Instant): Event? {
        return events
            .filter { it.datetimeUtc != null }
            .minByOrNull { event ->
                abs((event.datetimeUtc!! - now).inWholeMilliseconds)
            }
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnEventClicked -> navigateTo(NavigationEvent.ToEventDetail(action.eventId))
            is HomeUiAction.OnYearSelected -> onYearSelected(action.year)
            is HomeUiAction.OnRefreshFeaturedTab -> onRefreshFeatured()
            is HomeUiAction.OnRefreshUpcomingTab -> onRefreshUpcoming()
            is HomeUiAction.OnRefreshCompletedTab -> onRefreshCompleted()
        }
    }

    private fun onRefreshFeatured() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshingFeaturedTab = true) }
            eventRepository.forceRefreshFeaturedTab()
            _state.update { it.copy(isRefreshingFeaturedTab = false) }
        }
    }

    private fun onRefreshUpcoming() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshingUpcomingTab = true) }
            eventRepository.forceRefreshUpcomingTab()
            _state.update { it.copy(isRefreshingUpcomingTab = false) }
        }
    }

    private fun onRefreshCompleted() {
        val selectedYear = _state.value.selectedYear ?: currentYear
        viewModelScope.launch {
            _state.update { it.copy(isRefreshingCompletedTab = true) }
            eventRepository.forceRefreshCompletedTab(selectedYear)
            _state.update { it.copy(isRefreshingCompletedTab = false) }
        }
    }

    private fun navigateTo(event: NavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}