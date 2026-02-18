package com.berkekucuk.mmaapp.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.empty_upcoming_events
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingContainer(
    events: List<Event>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onEventClick: (String) -> Unit,
    listState: LazyListState
) {
    ListContainer(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        listState = listState,
        contentPadding = PaddingValues(top = 16.dp)
    ) {
        if (events.isEmpty()) {
            item(
                contentType = "EmptyState"
            ) {
                Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.empty_upcoming_events),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(
                items = events,
                key = { it.eventId },
                contentType = { "EventItem" }
            ) { event ->
                EventItem(
                    event = event,
                    onClick = onEventClick
                )
            }
        }
    }
}