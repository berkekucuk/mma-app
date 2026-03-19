package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.domain.model.Ranking
import com.berkekucuk.mmaapp.domain.model.WeightClass
import com.berkekucuk.mmaapp.presentation.components.ListContainer

@Composable
fun RankingContainer(
    rankings: Map<WeightClass, List<Ranking>>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onWeightClassClicked: (String, String) -> Unit,
    listState: LazyListState
) {
    val strings = LocalAppStrings.current

    ListContainer(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        listState = listState,
        contentPadding = PaddingValues(top = 10.dp),
        verticalSpacing = 10.dp
    ) {
        rankings.forEach { (weightClass, rankingsList) ->
            val weightClassId = rankingsList.firstOrNull()?.weightClassId ?: ""
            val isPoundForPound = weightClassId == "mens_p4p" || weightClassId == "womens_p4p"
            val champion = rankingsList.minByOrNull { it.rankNumber }
            val displayName = strings.toUpperCase(strings.weightClassDisplayName(weightClassId))
            item(key = "header_${weightClass.name}") {
                WeightClassCard(
                    weightClassName = displayName,
                    champion = champion,
                    isPoundForPound = isPoundForPound,
                    onWeightClassClicked = { onWeightClassClicked(weightClassId, weightClass.name) }
                )
            }
        }
    }
}
