package com.berkekucuk.mmaapp.presentation.screens.event_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.presentation.components.FightItem
import com.berkekucuk.mmaapp.presentation.components.ListContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FightsTab(
    fights: List<Fight>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onFightClick: (String) -> Unit,
    emptyMessage: String,
    listState: LazyListState
) {
    ListContainer(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        listState = listState,
        contentPadding = PaddingValues(top = 16.dp),
        verticalSpacing = 0.dp
    ) {
        if (fights.isEmpty()) {
            item(
                contentType = "EmptyState"
            ) {
                Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            itemsIndexed(
                items = fights,
                key = { _, fight -> fight.fightId },
                contentType = { _, _ -> "FightItem" }
            ) { index, fight ->
                FightItem(
                    fight = fight,
                    modifier = Modifier.height(108.dp),
                )
                if (index < fights.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = AppColors.dividerColor
                    )
                }
            }
        }
    }
}
