package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.repository.RankingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankingsViewModel(
    private val rankingsRepository: RankingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RankingsUiState())
    val state = _state.asStateFlow()

    init { fetchRankings() }

    private fun fetchRankings() {
        viewModelScope.launch {
            rankingsRepository.getRankings()
                .onSuccess { rankings ->
                    _state.update {
                        it.copy(isLoading = false, rankings = rankings)
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    fun onAction(action: RankingsUiAction) {
        when (action) {
            is RankingsUiAction.OnToggleExpand -> toggleExpand(action.weightClassId)
        }
    }

    private fun toggleExpand(weightClassId: String) {
        _state.update { state ->
            val expanded = state.expandedWeightClasses.toMutableSet()
            if (expanded.contains(weightClassId)) expanded.remove(weightClassId)
            else expanded.add(weightClassId)
            state.copy(expandedWeightClasses = expanded)
        }
    }
}