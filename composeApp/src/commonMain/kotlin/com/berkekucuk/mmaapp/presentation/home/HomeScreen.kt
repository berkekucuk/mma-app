package com.berkekucuk.mmaapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.berkekucuk.mmaapp.presentation.home.components.FightCardTab
import com.berkekucuk.mmaapp.presentation.home.components.TabRow
import com.berkekucuk.mmaapp.presentation.home.components.HomeTopAppBar
import com.berkekucuk.mmaapp.presentation.theme.AppColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onFightClick: (String) -> Unit,
    onFighterClick: (String) -> Unit
) {
    val uiState by viewModel.state.collectAsState()

    // Navigation event'lerini dinle
    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is NavigationEvent.ToFightDetail -> onFightClick(event.fightId)
                is NavigationEvent.ToFighterDetail -> onFighterClick(event.fighterId)
                is NavigationEvent.Back -> { /* Handle back if needed */ }
            }
        }
    }

    HomeScreen(
        state = uiState,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeUiAction) -> Unit
) {
    // Events boşsa mesaj göster
    if (state.events.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.PagerBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AppColors.TextPrimary)
        }
        return
    }

    val pagerState = rememberPagerState(
        initialPage = state.selectedIndex,
        pageCount = { state.events.size }
    )

    // Sayfa değişimini ViewModel'e bildir
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                onAction(HomeUiAction.OnPageChanged(page))
            }
    }

    // State'ten gelen selectedIndex değişirse pager'ı güncelle
    LaunchedEffect(state.selectedIndex) {
        if (pagerState.currentPage != state.selectedIndex) {
            pagerState.animateScrollToPage(state.selectedIndex)
        }
    }

    var selectedTab by remember { mutableStateOf(FightCardTab.MAIN_CARD) }

    // Şu anki event
    val currentEvent = state.events.getOrNull(pagerState.currentPage)

    Scaffold(
        topBar = {
            HomeTopAppBar(event = currentEvent)
        },
        containerColor = AppColors.PagerBackground
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            val event = state.events.getOrNull(page)

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Fight Card Tab Row
                TabRow(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                // Fight listesi buraya gelecek
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Fights will be here",
                        color = AppColors.TextSecondary
                    )
                }
            }
        }
    }
}
