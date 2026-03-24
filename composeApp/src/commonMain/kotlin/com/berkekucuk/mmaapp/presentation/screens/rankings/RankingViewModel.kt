package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.model.WeightClassRegistry
import com.berkekucuk.mmaapp.domain.repository.RankingRepository
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankingViewModel(
    private val repository: RankingRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RankingUiState())
    val state: StateFlow<RankingUiState> = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<RankingNavigationEvent>()
    val navigation = _navigation.asSharedFlow()
    private val womensWeightClassIds = setOf("womens_p4p", "sw", "w_flw", "w_bw")

    init {
        observeRankings()
        syncRankings()
    }

    private fun observeRankings() {
        viewModelScope.launch {
            repository.getRankings()
                .collect { grouped ->
                val mens = grouped.filter { it.key.lowercase() !in womensWeightClassIds }
                    .entries.sortedBy { WeightClassRegistry.sortOrderOf(it.key) }
                    .associate { it.key to it.value }
                val womens = grouped.filter { it.key.lowercase() in womensWeightClassIds }
                    .entries.sortedBy { WeightClassRegistry.sortOrderOf(it.key) }
                    .associate { it.key to it.value }
                _state.update { it.copy(mensRankings = mens, womensRankings = womens, isLoading = false) }
            }
        }
    }

    fun onAction(action: RankingUiAction) {
        when (action) {
            is RankingUiAction.OnWeightClassClicked -> navigateTo(RankingNavigationEvent.ToRankingDetail(action.weightClassId, action.weightClassName))
            is RankingUiAction.OnRefresh -> syncRankings(isRefreshing = true)
        }
    }

    private fun syncRankings(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = isRefreshing, error = null) }
            repository.syncRankings()
                .onSuccess {
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onFailure { e ->
                    val errorType = if (!repository.hasRankings()) {
                        when (e) {
                            is PostgrestRestException -> RankingError.UNKNOWN_ERROR
                            else -> RankingError.NETWORK_ERROR
                        }
                    } else null
                    _state.update { it.copy(isRefreshing = false, error = errorType) }
                }
        }
    }

    private fun navigateTo(event: RankingNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}