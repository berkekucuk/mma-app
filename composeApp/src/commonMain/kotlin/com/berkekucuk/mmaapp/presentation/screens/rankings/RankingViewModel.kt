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
        val mensMap = processListToSortedMap(mensList)
        val womensMap = processListToSortedMap(womensList)
        return Pair(mensMap, womensMap)
    }

    private fun processListToSortedMap(list: List<Ranking>): Map<WeightClass, List<Ranking>> {
        return list
            .filter { it.weightClass != null }
            .groupBy { it.weightClass!! }
            .mapValues { entry ->
                entry.value.sortedBy { it.rankNumber }
            }
            .toList()
            .sortedBy { it.first.sortOrder }
            .toMap()
    }

    fun onAction(action: RankingUiAction) {
        when (action) {
            is RankingUiAction.OnToggleExpand -> toggleExpand(action.weightClassId)
            is RankingUiAction.OnFighterClicked -> navigateTo(RankingNavigationEvent.ToFighterDetail(action.fighterId))
            is RankingUiAction.OnRefresh -> syncRankings()
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

    private fun syncRankings() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
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