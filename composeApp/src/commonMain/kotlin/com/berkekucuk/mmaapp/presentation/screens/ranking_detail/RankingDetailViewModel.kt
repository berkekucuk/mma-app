package com.berkekucuk.mmaapp.presentation.screens.ranking_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.domain.repository.RankingRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankingDetailViewModel(
    private val repository: RankingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.RankingDetail>()
    private val weightClassId = route.weightClassId
    private val weightClassName = route.weightClassName

    private val _state = MutableStateFlow(RankingDetailUiState(weightClassId = weightClassId, weightClassName = weightClassName))
    val state: StateFlow<RankingDetailUiState> = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<RankingDetailNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeRankings()
    }

    private fun observeRankings() {
        viewModelScope.launch {
            repository.getRankingsByWeightClass(weightClassId)
                .collect { rankingsList ->
                    val sorted = rankingsList.sortedBy { it.rankNumber }
                    _state.update {
                        it.copy(
                            rankedFighters = sorted,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onAction(action: RankingDetailUiAction) {
        when (action) {
            is RankingDetailUiAction.OnBackClicked -> navigateTo(RankingDetailNavigationEvent.Back)
            is RankingDetailUiAction.OnFighterClicked -> navigateTo(RankingDetailNavigationEvent.ToFighterDetail(action.fighterId))
            is RankingDetailUiAction.OnRefresh -> syncRankings()
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

    private fun navigateTo(event: RankingDetailNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}
