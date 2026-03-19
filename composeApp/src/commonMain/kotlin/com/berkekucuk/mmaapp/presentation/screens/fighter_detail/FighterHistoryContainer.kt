package com.berkekucuk.mmaapp.presentation.screens.fighter_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.presentation.components.ListContainer

@Composable
fun FighterHistoryContainer(
    fighter: Fighter,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onAction: (FighterDetailUiAction) -> Unit,
    extraBottomPadding: Dp = 0.dp,
) {
    ListContainer(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        contentPadding = PaddingValues(top = 10.dp),
        verticalSpacing = 0.dp,
        extraBottomPadding = extraBottomPadding,
    ) {
        val fights = fighter.fights
        val fighterId = fighter.fighterId
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                fights.forEachIndexed { index, fight ->
                    FightHistoryRow(
                        fight = fight,
                        fighterId = fighterId,
                        onClick = {
                            onAction(
                                FighterDetailUiAction.OnFightClicked(
                                    eventId = fight.eventId,
                                    fightId = fight.fightId,
                                )
                            )
                        }
                    )
                    if (index < fights.lastIndex) {
                        HorizontalDivider(
                            color = AppColors.dividerColor,
                            thickness = 0.8.dp,
                        )
                    }
                }
            }
        }
    }
}

