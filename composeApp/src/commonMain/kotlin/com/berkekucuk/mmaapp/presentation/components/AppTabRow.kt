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
import com.berkekucuk.mmaapp.core.presentation.AppColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTabRow(
    tabs: List<String>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    PrimaryTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = AppColors.topBarBackground,
        contentColor = AppColors.textPrimary,
        modifier = modifier,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                width = Dp.Unspecified,
                height = 3.dp,
                color = AppColors.ufcRed
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
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                selectedContentColor = AppColors.textPrimary,
                unselectedContentColor = AppColors.textSecondary
            )
        }
    }
}
