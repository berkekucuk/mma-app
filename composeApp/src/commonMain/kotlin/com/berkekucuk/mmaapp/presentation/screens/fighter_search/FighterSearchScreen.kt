package com.berkekucuk.mmaapp.presentation.screens.fighter_search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.Alignment
import com.berkekucuk.mmaapp.presentation.screens.profile.edit.ErrorBox
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FighterSearchScreenRoot(
    viewModel: FighterSearchViewModel = koinViewModel(),
    onNavigateToFighterDetail: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is FighterSearchNavigationEvent.ToFighterDetail -> onNavigateToFighterDetail(event.fighterId)
                is FighterSearchNavigationEvent.Back -> onBackClick()
            }
        }
    }

    FighterSearchScreen(
        state = uiState,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FighterSearchScreen(
    state: FighterSearchUiState,
    onAction: (FighterSearchUiAction) -> Unit,
) {
    val strings = LocalAppStrings.current
    val focusRequester = remember { FocusRequester() }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(state.query, TextRange(state.query.length))) }
    val onBackClicked = remember(onAction) { { onAction(FighterSearchUiAction.OnBackClicked) } }
    val onClearQuery = remember(onAction) { { onAction(FighterSearchUiAction.OnClearQuery) } }
    val onFighterClicked = remember(onAction) { { fighterId: String -> onAction(FighterSearchUiAction.OnFighterClicked(fighterId)) } }

    LaunchedEffect(state.query) {
        if (textFieldValue.text != state.query) {
            textFieldValue = TextFieldValue(state.query, TextRange(state.query.length))
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            Column(
                modifier = Modifier.background(AppColors.rankingTopBarGradient)
            ){
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.contentDescriptionBack,
                                tint = AppColors.textPrimary,
                            )
                        }
                    },
                    title = {
                        BasicTextField(
                            value = textFieldValue,
                            onValueChange = { newValue ->
                                textFieldValue = newValue
                                onAction(FighterSearchUiAction.OnQueryChanged(newValue.text))
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = AppColors.textPrimary,
                                fontSize = 18.sp,
                            ),
                            cursorBrush = SolidColor(AppColors.ufcRed),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (state.query.isEmpty()) {
                                        Text(
                                            text = strings.fighterSearchPlaceholder,
                                            color = AppColors.textSecondary,
                                            fontSize = 18.sp,
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                        )
                    },
                    actions = {
                        if (state.query.isNotEmpty()) {
                            IconButton(onClick = onClearQuery) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    tint = AppColors.textSecondary,
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = AppColors.textPrimary,
                        titleContentColor = AppColors.textPrimary
                    ),
                )
            }

        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.error != null -> {
                    val errorMessage = when (state.error) {
                        FighterSearchError.NETWORK_ERROR -> strings.errorNetwork
                        FighterSearchError.UNKNOWN_ERROR -> strings.errorUnknown
                    }
                    ErrorBox(
                        message = errorMessage,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
                state.results.isNotEmpty() -> {
                    FighterResultListContainer(
                        fighters = state.results,
                        onFighterClicked = onFighterClicked,
                    )
                }
                state.query.length >= 2 -> {
                    ErrorBox(
                        message = strings.fighterSearchEmpty,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}
