package com.berkekucuk.mmaapp.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.presentation.screens.login.LoginScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val authState by viewModel.authState.collectAsState()

    when (val state = authState) {
        is AuthState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.pagerBackground),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppColors.ufcRed)
            }
        }
        is AuthState.Unauthenticated -> {
            LoginScreen()
        }
        is AuthState.Authenticated -> {
            ProfileScreen(
                email = state.email,
                onSignOutClick = { viewModel.onSignOutClick() }
            )
        }
    }
}

@Composable
fun ProfileScreen(
    email: String?,
    onSignOutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.pagerBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Signed in",
                style = MaterialTheme.typography.headlineMedium,
                color = AppColors.textPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (!email.isNullOrBlank()) {
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppColors.textPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onSignOutClick) {
                Text("Sign out")
            }
        }
    }
}