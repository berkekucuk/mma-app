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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
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
    viewModel: MenuViewModel = koinViewModel(),
    supabaseClient: SupabaseClient = koinInject()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is MenuNavigationEvent.ToProfile -> onNavigateToProfile(event.userId)
                is MenuNavigationEvent.ToProfileEdit -> onNavigateToProfileEdit(event.userId)
                MenuNavigationEvent.ToSettings -> onNavigateToSettings()
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

    val (showSignInSheet, setShowSignInSheet) = remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onAction(MenuUiAction.OnResumeCheckSettings)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val onSignInClick = remember(setShowSignInSheet) { { setShowSignInSheet(true) } }
    val onDismissSignIn = remember(setShowSignInSheet) { { setShowSignInSheet(false) } }
    val onStartSignIn = remember(setShowSignInSheet, onStartGoogleSignIn) {
        {
            setShowSignInSheet(false)
            onStartGoogleSignIn()
        }
    }

    val onProfileClick = remember(onAction) { { onAction(MenuUiAction.OnOpenProfileClicked) } }
    val onProfileEditClick = remember(onAction) { { onAction(MenuUiAction.OnOpenProfileEditClicked) } }
    val onNotificationsClick = remember(onAction) { { onAction(MenuUiAction.OnNotificationsClicked) } }
    val onSettingsClick = remember(onAction) { { onAction(MenuUiAction.OnSettingsClicked) } }
    val onSignOutClick = remember(onAction) { { onAction(MenuUiAction.OnSignOutClicked) } }
    val onDummyClick = remember { { } }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = strings.menuTitle,
                        fontSize = 22.sp,
                        fontFamily = AppFonts.RobotoCondensed,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.topBarBackground,
                    titleContentColor = AppColors.textPrimary,
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

            HorizontalDivider(color = AppColors.dividerColor)
            MenuItemRow(
                icon = Icons.Filled.Person,
                title = strings.menuItemUsers,
                onClick = onDummyClick
            )
            HorizontalDivider(color = AppColors.dividerColor)
            MenuItemRow(
                icon = if (state.notificationsEnabled) Icons.Filled.Notifications else Icons.Filled.NotificationsOff,
                title = strings.menuItemNotifications,
                subtitle = if (state.notificationsEnabled) null else strings.menuNotificationsDisabled,
                onClick = onNotificationsClick
            )
            HorizontalDivider(color = AppColors.dividerColor)
            MenuItemRow(
                icon = Icons.Filled.Settings,
                title = strings.menuItemSettings,
                onClick = onSettingsClick
            )
            HorizontalDivider(color = AppColors.dividerColor)

            Spacer(modifier = Modifier.weight(1f))

            if (state.authState is AuthState.Authenticated) {
                HorizontalDivider(color = AppColors.dividerColor)
                MenuItemRow(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = strings.profileSignOut,
                    tint = AppColors.loseColor,
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
            containerColor = AppColors.dropdownMenuBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = strings.menuSignInButton,
                    color = AppColors.textPrimary,
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
                    color = AppColors.textSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
