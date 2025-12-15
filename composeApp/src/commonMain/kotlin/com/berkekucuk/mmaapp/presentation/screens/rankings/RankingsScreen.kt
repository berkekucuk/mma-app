package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.berkekucuk.mmaapp.core.presentation.AppColors

@Composable
fun RankingsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.pagerBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Rankings",
            style = MaterialTheme.typography.headlineMedium,
            color = AppColors.textPrimary
        )
    }
}