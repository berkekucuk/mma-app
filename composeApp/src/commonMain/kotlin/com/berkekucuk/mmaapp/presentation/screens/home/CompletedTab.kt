package com.berkekucuk.mmaapp.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import kotlinx.coroutines.delay
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.empty_events_for_year
import mmaapp.composeapp.generated.resources.select_year
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedTab(
    completedEvents: List<Event>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onEventClick: (String) -> Unit,
    availableYears: List<Int>,
    selectedYear: Int?,
    isYearLoading: Boolean,
    onYearSelected: (Int) -> Unit,
    listState: LazyListState
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    var showEmptyMessage by remember { mutableStateOf(false) }

    LaunchedEffect(isYearLoading, completedEvents) {
        if (!isYearLoading && completedEvents.isEmpty()) {
            delay(150)
            showEmptyMessage = true
        } else {
            showEmptyMessage = false
        }
    }

    ListContainer(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        listState = listState,
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        item(key = "year_filter_row") {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    FilterChip(
                        selected = true,
                        onClick = {},
                        label = {
                            Text(
                                text = selectedYear?.toString()
                                    ?: stringResource(Res.string.select_year),
                                fontWeight = FontWeight.Medium
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            true
                        ),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppColors.dropdownMenuBackground,
                            selectedLabelColor = AppColors.textPrimary,
                            selectedTrailingIconColor = AppColors.textPrimary
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.heightIn(max = 300.dp),
                        containerColor = AppColors.dropdownMenuBackground
                    ) {
                        availableYears.forEach { year ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = year.toString(),
                                        color = AppColors.textPrimary
                                    )
                                },
                                onClick = {
                                    onYearSelected(year)
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }
        }

        if (isYearLoading) {
            item(contentType = "Loader") {
                Box(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .background(AppColors.pagerBackground),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.ufcRed)
                }
            }
        } else if (completedEvents.isEmpty()) {
            if (showEmptyMessage) {
                item(contentType = "EmptyState") {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(
                                Res.string.empty_events_for_year,
                                selectedYear.toString()
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(
                items = completedEvents,
                key = { it.eventId },
                contentType = { "EventItem" }
            ) { event ->
                EventItem(
                    event = event,
                    onClick = { onEventClick(event.eventId) }
                )
            }
        }
    }
}

