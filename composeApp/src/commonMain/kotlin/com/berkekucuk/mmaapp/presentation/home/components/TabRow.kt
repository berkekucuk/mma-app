package com.berkekucuk.mmaapp.presentation.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.presentation.theme.AppColors

enum class FightCardTab(val title: String) {
    MAIN_CARD("Main Card"),
    PRELIMS("Prelims")
}

@Composable
fun TabRow(
    selectedTab: FightCardTab,
    onTabSelected: (FightCardTab) -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryTabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = modifier.fillMaxWidth(),
        containerColor = AppColors.TopBarBackground,
        contentColor = AppColors.TextPrimary,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTab.ordinal),
                color = AppColors.UfcRed,
                width = 180.dp
            )
        },
        divider = {}
    ) {
        FightCardTab.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab.title,
                        fontSize = 16.sp,
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == tab) AppColors.TextPrimary else AppColors.TextSecondary
                    )
                }
            )
        }
    }
}
