package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.model.Ranking
import com.berkekucuk.mmaapp.domain.model.WeightClass
import com.berkekucuk.mmaapp.domain.repository.RankingRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankingViewModel(
    private val repository: RankingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RankingUiState())
    val state: StateFlow<RankingUiState> = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<RankingNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    private val womensWeightClassIds = setOf("womens_p4p", "SW", "W_FLW", "W_BW")

    init {
        observeRankings()
        syncRankings()
    }

    private fun observeRankings() {
        viewModelScope.launch {
            repository.getRankings()
                .map { rankings ->
                    categorizeAndSort(rankings)
                }
                .collect { (mens, womens) ->
                    _state.update { it.copy(
                        mensRankings = mens,
                        womensRankings = womens,
                        isLoading = false
                    ) }
                }
        }
    }

    private fun categorizeAndSort(rankings: List<Ranking>): Pair<Map<WeightClass, List<Ranking>>, Map<WeightClass, List<Ranking>>> {
        val mensList = rankings.filter { it.weightClassId !in womensWeightClassIds }
        val womensList = rankings.filter { it.weightClassId in womensWeightClassIds }
        val mensMap = processListToMap(mensList)
        val womensMap = processListToMap(womensList)
        return Pair(mensMap, womensMap)
    }

    private fun processListToMap(list: List<Ranking>): Map<WeightClass, List<Ranking>> {
        return list
            .filter { it.weightClass != null }
            .groupBy { it.weightClass!! }
            .toList()
            .sortedBy { it.first.sortOrder }
            .toMap()
    }

    fun onAction(action: RankingUiAction) {
        when (action) {
            is RankingUiAction.OnWeightClassClicked -> navigateTo(RankingNavigationEvent.ToRankingDetail(action.weightClassId, action.weightClassName))
            is RankingUiAction.OnRefresh -> syncRankings(isRefreshing = true)
        }
    }

    private fun syncRankings(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = isRefreshing) }
            repository.syncRankings()
                .onSuccess {
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onFailure {
                    _state.update { it.copy(isRefreshing = false) }
                }
        }
    }

    private fun navigateTo(event: RankingNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}