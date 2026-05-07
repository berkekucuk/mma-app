package com.berkekucuk.mmaapp.presentation.screens.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings
import com.berkekucuk.mmaapp.domain.model.AuthState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Apple
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithApple
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreenRoot(
    onNavigateToProfile: (String) -> Unit,
    onNavigateToProfileEdit: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    viewModel: MenuViewModel = koinViewModel(),
    supabaseClient: SupabaseClient = koinInject()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is MenuNavigationEvent.ToProfile -> onNavigateToProfile(event.userId)
                is MenuNavigationEvent.ToProfileEdit -> onNavigateToProfileEdit()
                is MenuNavigationEvent.ToSettings -> onNavigateToSettings()
                is MenuNavigationEvent.ToLeaderboard -> onNavigateToLeaderboard()
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val signInWithGoogleAction = supabaseClient.composeAuth.rememberSignInWithGoogle(
        onResult = {},
        fallback = {
            coroutineScope.launch {
                try {
                    supabaseClient.auth.signInWith(Google)
                } catch (_: Exception) { }
            }
        }
    )

    val signInWithAppleAction = supabaseClient.composeAuth.rememberSignInWithApple(
        onResult = {},
        fallback = {
            coroutineScope.launch {
                try {
                    supabaseClient.auth.signInWith(Apple)
                } catch (_: Exception) { }
            }
        }
    )

    val onStartGoogleSignIn = remember(signInWithGoogleAction) {
        {
            try {
                signInWithGoogleAction.startFlow()
            } catch (_: Exception) { }
        }
    }

    val onStartAppleSignIn = remember(signInWithAppleAction) {
        {
            try {
                signInWithAppleAction.startFlow()
            } catch (_: Exception) { }
        }
    }

    MenuScreen(
        state = state,
        onAction = viewModel::onAction,
        onStartGoogleSignIn = onStartGoogleSignIn,
        onStartAppleSignIn = onStartAppleSignIn
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    state: MenuUiState,
    onAction: (MenuUiAction) -> Unit,
    onStartGoogleSignIn: () -> Unit,
    onStartAppleSignIn: () -> Unit,
) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current

    val (showSignInSheet, setShowSignInSheet) = remember { mutableStateOf(false) }

    val onSignInClick = remember(setShowSignInSheet) { { setShowSignInSheet(true) } }
    val onDismissSignIn = remember(setShowSignInSheet) { { setShowSignInSheet(false) } }
    val onStartGoogleSignInClick = remember(setShowSignInSheet, onStartGoogleSignIn) {
        {
            setShowSignInSheet(false)
            onStartGoogleSignIn()
        }
    }
    
    val onStartAppleSignInClick = remember(setShowSignInSheet, onStartAppleSignIn) {
        {
            setShowSignInSheet(false)
            onStartAppleSignIn()
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

    if (showSignInSheet) {
        SignInBottomSheet(
            onDismiss = onDismissSignIn,
            onStartGoogleSignIn = onStartGoogleSignInClick,
            onStartAppleSignIn = onStartAppleSignInClick
        )
    }
}