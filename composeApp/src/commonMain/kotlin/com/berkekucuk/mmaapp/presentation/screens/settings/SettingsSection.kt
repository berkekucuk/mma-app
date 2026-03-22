package com.berkekucuk.mmaapp.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppTypography

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Text(
        text = title,
        style = AppTypography.titleMedium,
        color = AppColors.textSecondary,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.fightItemBackground)
    ) {
        content()
    }
}