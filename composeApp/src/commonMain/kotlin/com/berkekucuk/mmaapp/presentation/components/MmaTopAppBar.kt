package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.berkekucuk.mmaapp.presentation.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MmaTopAppBar(
    title: String
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppColors.TopBarBackground
        )
    )
}
