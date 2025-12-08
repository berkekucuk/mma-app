package com.berkekucuk.mmaapp.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.berkekucuk.mmaapp.domain.model.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsTab(
    events: List<Event>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onEventClick: (String) -> Unit,
    emptyMessage: String
) {
    EventsListContainer(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        if (events.isEmpty()) {
            item {
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
                items = events,
                key = { it.id }
            ) { event ->
                EventItem(
                    event = event,
                    onClick = { onEventClick(event.id) }
                )
            }
        }
    }
}
