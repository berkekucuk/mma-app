package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings

@Composable
fun ErrorSnackbarEffect(
    error: Any?,
    message: String,
    snackbarHostState: SnackbarHostState,
    onRetry: () -> Unit,
) {
    val strings = LocalAppStrings.current
    LaunchedEffect(error) {
        if (error == null) return@LaunchedEffect
        while (true) {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = strings.retry,
                duration = SnackbarDuration.Indefinite,
            )
            if (result == SnackbarResult.ActionPerformed) {
                onRetry()
            } else break
        }
    }
}
