package com.berkekucuk.mmaapp.presentation.screens.fighter_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.presentation.components.FighterImage

@Composable
fun FighterTopBarTitle(
    imageUrl: String,
    name: String,
    countryCode: String,
    nickname: String?,
    showQuotes: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        FighterImage(
            imageUrl = imageUrl,
            name = name,
            countryCode = countryCode,
            alignment = Alignment.Start,
        )
        Column {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                color = AppColors.textPrimary,
            )
            if (!nickname.isNullOrBlank()) {
                Text(
                    text = if (showQuotes) "\"$nickname\"" else nickname,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.textSecondary,
                )
            }
        }
    }
}
