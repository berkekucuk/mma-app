package com.berkekucuk.mmaapp.presentation.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.presentation.components.GoogleSignInButton
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreenRoot(
    onNavigateToProfile: (String) -> Unit,
    onNavigateToProfileEdit: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    viewModel: MenuViewModel = koinViewModel(),
    supabaseClient: SupabaseClient = koinInject()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is MenuNavigationEvent.ToProfile -> onNavigateToProfile(event.userId)
                is MenuNavigationEvent.ToProfileEdit -> onNavigateToProfileEdit(event.userId)
                is MenuNavigationEvent.ToSettings -> onNavigateToSettings()
                is MenuNavigationEvent.ToLeaderboard -> onNavigateToLeaderboard()
            }
        }
    }

    val signInAction = supabaseClient.composeAuth.rememberSignInWithGoogle(
        onResult = {},
        fallback = {}
    )

    val onStartGoogleSignIn = remember(signInAction) {
        {
            try {
                signInAction.startFlow()
            } catch (_: Exception) {
            }
        }
    }

    MenuScreen(
        state = state,
        onAction = viewModel::onAction,
        onStartGoogleSignIn = onStartGoogleSignIn
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    state: MenuUiState,
    onAction: (MenuUiAction) -> Unit,
    onStartGoogleSignIn: () -> Unit,
) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current

    val (showSignInSheet, setShowSignInSheet) = remember { mutableStateOf(false) }

    val onSignInClick = remember(setShowSignInSheet) { { setShowSignInSheet(true) } }
    val onDismissSignIn = remember(setShowSignInSheet) { { setShowSignInSheet(false) } }
    val onStartSignIn = remember(setShowSignInSheet, onStartGoogleSignIn) {
        {
            setShowSignInSheet(false)
            onStartGoogleSignIn()
        }
    }

    val onProfileClick = remember(onAction) { { onAction(MenuUiAction.OnProfileClicked) } }
    val onProfileEditClick = remember(onAction) { { onAction(MenuUiAction.OnProfileEditClicked) } }
    val onSettingsClick = remember(onAction) { { onAction(MenuUiAction.OnSettingsClicked) } }
    val onLeaderboardClick = remember(onAction) { { onAction(MenuUiAction.OnLeaderboardClicked) } }
    val onSignOutClick = remember(onAction) { { onAction(MenuUiAction.OnSignOutClicked) } }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.pagerBackground,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = strings.menuTitle,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.topBarBackground,
                    titleContentColor = colors.textPrimary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.authState) {
                is AuthState.Loading -> {}

                is AuthState.Unauthenticated -> {
                    UnauthenticatedTopHeader(onSignInClick = onSignInClick)
                }

                is AuthState.Authenticated -> {
                    AuthenticatedSection(
                        name = state.name ?: "",
                        username = state.username,
                        avatarUrl = state.avatarUrl,
                        onProfileClick = onProfileClick,
                        onProfileEditClick = onProfileEditClick,
                    )
                }
            }

            HorizontalDivider(color = colors.dividerColor)
            MenuItemRow(
                icon = Icons.Filled.Person,
                title = strings.menuItemLeaderboard,
                onClick = onLeaderboardClick
            )

            HorizontalDivider(color = colors.dividerColor)
            MenuItemRow(
                icon = Icons.Filled.Settings,
                title = strings.menuItemSettings,
                onClick = onSettingsClick
            )
            HorizontalDivider(color = colors.dividerColor)

            Spacer(modifier = Modifier.weight(1f))

            if (state.authState is AuthState.Authenticated) {
                HorizontalDivider(color = colors.dividerColor)
                MenuItemRow(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = strings.profileSignOut,
                    tint = colors.loseColor,
                    onClick = onSignOutClick,
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    if (showSignInSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = onDismissSignIn,
            sheetState = sheetState,
            containerColor = colors.dropdownMenuBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = strings.menuSignInButton,
                    color = colors.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                GoogleSignInButton(
                    onClick = onStartSignIn,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = strings.menuSignInTerms,
                    color = colors.textSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}