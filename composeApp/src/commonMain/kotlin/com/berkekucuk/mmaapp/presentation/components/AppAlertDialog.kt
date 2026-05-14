package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors

@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: (() -> Unit)? = null,
    title: String? = null,
    text: String? = null,
    content: @Composable (() -> Unit)? = null,
    confirmText: String,
    dismissText: String? = null,
    isDestructive: Boolean = false,
    confirmEnabled: Boolean = true,
    isConfirmLoading: Boolean = false,
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
            if (content != null) {
                content()
            } else if (text != null) {
                Text(
                    text = text,
                    color = colors.textSecondary
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
                enabled = confirmEnabled && !isConfirmLoading
            ) {
                if (isConfirmLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), 
                        strokeWidth = 2.dp, 
                        color = if (isDestructive) colors.ufcRed else colors.winnerFrame
                    )
                } else {
                    Text(
                        text = confirmText,
                        color = if (!confirmEnabled) colors.textSecondary.copy(alpha = 0.5f) else if (isDestructive) colors.ufcRed else colors.winnerFrame
                    )
                }
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
