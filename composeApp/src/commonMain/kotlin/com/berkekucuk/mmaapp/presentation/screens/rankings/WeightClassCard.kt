package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppTypography
import com.berkekucuk.mmaapp.domain.model.Ranking
import com.berkekucuk.mmaapp.presentation.components.FighterPortrait

@Composable
fun WeightClassCard(
    weightClassName: String,
    champion: Ranking?,
    onWeightClassClicked: () -> Unit
) {
    val championFighter = champion?.fighter

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onWeightClassClicked() },
        border = BorderStroke(1.dp, AppColors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.fightItemBackground)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = weightClassName.uppercase(),
                    style = AppTypography.labelMedium,
                    color = AppColors.textSecondary,
                )

                if (championFighter != null) {
                    Spacer(Modifier.height(8.dp))
                    FighterPortrait(
                        name = championFighter.name,
                        imageUrl = championFighter.imageUrl,
                        countryCode = championFighter.countryCode,
                        record = championFighter.record.toString(),
                        alignment = Alignment.Start,
                        nameFontSize = 16.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = AppColors.textSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}