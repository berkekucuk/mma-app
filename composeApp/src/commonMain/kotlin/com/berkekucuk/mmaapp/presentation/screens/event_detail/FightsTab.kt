package com.berkekucuk.mmaapp.presentation.screens.event_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        contentPadding = PaddingValues(top = 16.dp)
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
            items(
                items = fights,
                key = { it.fightId },
                contentType = { "FightItem" }
            ) { fight ->
                FightItem(
                    fight = fight,
                    modifier = Modifier.height(108.dp)
                )
            }
        }
    }
}