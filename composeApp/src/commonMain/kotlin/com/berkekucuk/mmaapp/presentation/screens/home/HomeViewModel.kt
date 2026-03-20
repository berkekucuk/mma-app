package com.berkekucuk.mmaapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.core.utils.DateTimeProvider
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val eventRepository: EventRepository,
    private val dateTimeProvider: DateTimeProvider
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<HomeNavigationEvent>()
    val navigation = _navigation.asSharedFlow()
    private var isMinSplashTimePassed = false
    private var isDataReady = false
    private val selectedYearFlow = MutableStateFlow(0)

    init {
        val currentYear = dateTimeProvider.currentYear
        selectedYearFlow.value = currentYear
        _state.update {
            it.copy(
                selectedYear = currentYear,
                availableYears = (1993..currentYear).toList().reversed(),
            )
        }

        viewModelScope.launch {
            delay(800)
            isMinSplashTimePassed = true
            checkLoadingState()
        }

        observeUpcomingEvents()
        observeCompletedEvents()
        syncEvents()
    }

    private fun checkLoadingState() {
        if (isMinSplashTimePassed && isDataReady) {
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun observeUpcomingEvents() {
        viewModelScope.launch {
            eventRepository.getUpcomingEvents()
                .catch { error ->
                    println("Error observing upcoming events: $error")
                    isDataReady = true
                    checkLoadingState()
                }
                .collect { events ->
                    _state.update { it.copy(upcomingEvents = events) }
                    isDataReady = true
                    checkLoadingState()
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCompletedEvents() {
        viewModelScope.launch {
            selectedYearFlow
                .flatMapLatest { year ->
                    eventRepository.getCompletedEventsByYear(year)
                }
                .catch { error ->
                    println("Error observing completed events: $error")
                }
                .collect { events ->
                    _state.update { state ->
                        state.copy(
                            completedEvents = events,
                            isYearLoading = if (state.isYearLoading && events.isNotEmpty()) false
                                            else state.isYearLoading,
                        )
                    }
                }
        }
    }

    private fun syncEvents() {
        viewModelScope.launch {
            eventRepository.syncEvents()
        }
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnEventClicked -> navigateTo(HomeNavigationEvent.ToEventDetail(action.eventId))
            is HomeUiAction.OnYearSelected -> onYearSelected(action.year)
            is HomeUiAction.OnRefreshUpcomingTab -> onRefreshUpcomingTab()
            is HomeUiAction.OnRefreshCompletedTab -> onRefreshCompletedTab()
            is HomeUiAction.OnSearchClicked -> navigateTo(HomeNavigationEvent.ToFighterSearch)
        }
    }

    private fun onYearSelected(year: Int) {
        if (_state.value.selectedYear == year) return

        _state.update { it.copy(selectedYear = year, isYearLoading = true) }
        selectedYearFlow.value = year

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
