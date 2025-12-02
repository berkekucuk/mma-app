package com.berkekucuk.mmaapp.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.berkekucuk.mmaapp.presentation.components.MmaTopAppBar
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

@Composable
private fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            MmaTopAppBar(title = "MMA")
        },
        containerColor = AppColors.PagerBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

        }
    }
}




