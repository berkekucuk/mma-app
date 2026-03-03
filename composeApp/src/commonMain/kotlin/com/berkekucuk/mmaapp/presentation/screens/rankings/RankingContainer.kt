package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.domain.model.Ranking
import com.berkekucuk.mmaapp.domain.model.WeightClass
import com.berkekucuk.mmaapp.presentation.components.ListContainer

@Composable
fun RankingContainer(
    rankings: Map<WeightClass, List<Ranking>>,
    expandedWeightClasses: Set<String>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onToggleExpand: (String) -> Unit,
    onFighterClicked: (String) -> Unit,
    listState: LazyListState
) {
    ListContainer(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        listState = listState,
        contentPadding = PaddingValues(top = 10.dp),
        verticalSpacing = 10.dp
    ) {
        rankings.forEach { (weightClass, rankingsList) ->
            val weightClassId = rankingsList.firstOrNull()?.weightClassId ?: ""
            val champion = rankingsList.firstOrNull { it.rankNumber == 0 }
            val rankedFighters = rankingsList.filter { it.rankNumber > 0 }

            item(key = "header_${weightClass.name}") {
                WeightClassCard(
                    weightClassName = weightClass.name,
                    weightClassId = weightClassId,
                    champion = champion,
                    rankedFighters = rankedFighters,
                    isExpanded = expandedWeightClasses.contains(weightClassId),
                    onToggleExpand = { onToggleExpand(weightClassId) },
                    onFighterClicked = onFighterClicked
                )
            }
        }
    }
}
