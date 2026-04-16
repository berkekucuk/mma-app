package com.berkekucuk.mmaapp.presentation.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings
import com.berkekucuk.mmaapp.domain.model.AuthState
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
    viewModel: MenuViewModel = koinViewModel(),
    supabaseClient: SupabaseClient = koinInject()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is MenuNavigationEvent.ToProfile -> onNavigateToProfile(event.userId)
                is MenuNavigationEvent.ToProfileEdit -> onNavigateToProfileEdit(event.userId)
            }
        }
    }

    val signInAction = supabaseClient.composeAuth.rememberSignInWithGoogle(
        onResult = {},
        fallback = {}
    )

    MenuScreen(
        state = state,
        onAction = viewModel::onAction,
        onStartGoogleSignIn = {
            try {
                signInAction.startFlow()
            } catch (_: Exception) {
            }
        }
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.pagerBackground,
        topBar = {
            TopAppBar(
                title = { Text(text = "Menu") },
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
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign in and continue from here",
                color = colors.textSecondary,
                modifier = Modifier.fillMaxWidth()
            )

            when (state.authState) {
                AuthState.Loading -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    CircularProgressIndicator(color = colors.ufcRed)
                }

                AuthState.Unauthenticated -> {
                    Button(
                        onClick = onStartGoogleSignIn,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = strings.loginSignInGoogle)
                    }
                }

                is AuthState.Authenticated -> {
                    Button(
                        onClick = { onAction(MenuUiAction.OnOpenProfileClicked) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Open Profile")
                    }

                    Button(
                        onClick = { onAction(MenuUiAction.OnOpenProfileEditClicked) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Profile Settings")
                    }

                    Button(
                        onClick = { onAction(MenuUiAction.OnSignOutClicked) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = strings.profileSignOut)
                    }
                }
            }
        }
    }
}
