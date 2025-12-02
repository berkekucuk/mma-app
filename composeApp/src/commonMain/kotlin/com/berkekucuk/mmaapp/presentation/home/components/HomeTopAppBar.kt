package com.berkekucuk.mmaapp.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.berkekucuk.mmaapp.presentation.theme.AppColors
import com.berkekucuk.mmaapp.domain.model.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    event: Event?
) {
    TopAppBar(
        title = {
            if (event != null) {
                Column {
                    Text(
                        text = extractEventNumber(event.name),
                        color = AppColors.TextPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = extractEventSubtitle(event.name),
                        color = AppColors.TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppColors.TopBarBackground
        )
    )
}

private fun extractEventNumber(name: String): String {
    return name.substringBefore(":").trim().ifEmpty { name }
}

private fun extractEventSubtitle(name: String): String {
    return if (name.contains(":")) {
        name.substringAfter(":").trim()
    } else {
        ""
    }
}