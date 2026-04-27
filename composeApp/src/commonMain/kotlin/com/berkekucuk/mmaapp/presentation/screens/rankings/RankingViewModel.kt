package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.core.utils.AppErrorMapper
import com.berkekucuk.mmaapp.domain.repository.WeightClassRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankingViewModel(
    private val repository: WeightClassRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RankingUiState())
    val state: StateFlow<RankingUiState> = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<RankingNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeRankings()
        syncRankings()
    }

    private fun observeRankings() {
        viewModelScope.launch {
            repository.getAllWeightClasses()
                .collect { weightClasses ->
                    _state.update { it.copy(weightClasses = weightClasses) }
                }
        }
    }

    fun onAction(action: RankingUiAction) {
        when (action) {
            is RankingUiAction.OnWeightClassClicked -> navigateTo(RankingNavigationEvent.ToRankingDetail(action.weightClassId))
            is RankingUiAction.OnRefresh -> syncRankings(isRefreshing = true)
        }
    }

    private fun syncRankings(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = isRefreshing, error = null) }
            repository.syncWeightClasses()
                .onSuccess {
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onFailure { e ->
                    val error = if (!repository.hasData()) {
                        AppErrorMapper.map(e)
                    } else null
                    _state.update { it.copy(isRefreshing = false, error = error) }
                }
        }
    }

    private fun navigateTo(event: RankingNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}