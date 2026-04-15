package com.berkekucuk.mmaapp.presentation.screens.profile_edit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors

@Composable
fun SaveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSaving: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.winnerFrame,
            contentColor = AppColors.textPrimary,
            disabledContainerColor = AppColors.winnerFrame.copy(alpha = 0.5f),
            disabledContentColor = AppColors.winnerFrame.copy(alpha = 0.7f)
        ),
        enabled = enabled && !isSaving
    ) {
        if (isSaving) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = AppColors.textPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                color = AppColors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}