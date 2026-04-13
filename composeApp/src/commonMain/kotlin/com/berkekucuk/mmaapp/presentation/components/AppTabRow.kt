package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.berkekucuk.mmaapp.core.presentation.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTabRow(
    tabs: List<String>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    containerColor: Color = AppTheme.colors.topBarBackground,
) {
    PrimaryTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = containerColor,
        contentColor = AppTheme.colors.textPrimary,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                width = Dp.Unspecified,
                height = 2.dp,
                color = AppTheme.colors.ufcRed
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                selectedContentColor = AppTheme.colors.textPrimary,
                unselectedContentColor = AppTheme.colors.textSecondary
            )
        }
    }
}
