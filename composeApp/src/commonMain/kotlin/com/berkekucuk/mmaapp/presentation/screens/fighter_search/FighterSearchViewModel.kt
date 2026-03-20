package com.berkekucuk.mmaapp.presentation.screens.fighter_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.repository.FighterRepository
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class FighterSearchViewModel(
    private val repository: FighterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FighterSearchUiState())
    val state = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<FighterSearchNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeSearchQuery()
    }

    private fun observeSearchQuery(){
        viewModelScope.launch {
            _state
                .map { it.query }
                .distinctUntilChanged()
                .debounce(500)
                .collectLatest { query ->
                    if (query.length >= 2) {
                        search(query)
                    } else {
                        _state.update { it.copy(results = emptyList(), isLoading = false, isEmpty = false) }
                    }
                }
        }
    }

    private suspend fun search(query: String) {
        _state.update { it.copy(isLoading = true, isEmpty = false, error = null) }
        repository.searchFighters(query)
            .onSuccess { results ->
                _state.update { it.copy(results = results, isLoading = false, isEmpty = results.isEmpty()) }
            }
            .onFailure { e ->
                val errorType = when (e) {
                    is PostgrestRestException -> FighterSearchError.UNKNOWN_ERROR
                    else -> FighterSearchError.NETWORK_ERROR
                }
                _state.update { it.copy(isLoading = false, isEmpty = false, error = errorType) }
            }
    }

    fun onAction(action: FighterSearchUiAction) {
        when (action) {
            is FighterSearchUiAction.OnQueryChanged -> _state.update { it.copy(query = action.query, error = null) }
            is FighterSearchUiAction.OnClearQuery -> _state.update { it.copy(query = "", results = emptyList(), isEmpty = false, error = null) }
            is FighterSearchUiAction.OnFighterClicked -> navigateTo(FighterSearchNavigationEvent.ToFighterDetail(action.fighterId))
            is FighterSearchUiAction.OnBackClicked -> navigateTo(FighterSearchNavigationEvent.Back)
        }
    }

    private fun navigateTo(event: FighterSearchNavigationEvent) {
        viewModelScope.launch { _navigation.emit(event) }
    }
}
