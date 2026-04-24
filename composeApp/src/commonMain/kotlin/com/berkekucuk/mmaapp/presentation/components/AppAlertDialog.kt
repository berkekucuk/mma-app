package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors

@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: (() -> Unit)? = null,
    title: String? = null,
    text: String,
    confirmText: String,
    dismissText: String? = null,
) {
    val colors = LocalAppColors.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = title?.let {
            {
                Text(
                    text = it,
                    color = colors.textPrimary
                )
            }
        },
        text = {
            Text(
                text = text,
                color = colors.textSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = confirmText, color = colors.winnerFrame)
            }
        },
        dismissButton = dismissText?.let {
            {
                TextButton(onClick = onDismissClick ?: onDismissRequest) {
                    Text(text = it, color = colors.textSecondary)
                }
            }
        },
        containerColor = colors.fightItemBackground,
    )
}
