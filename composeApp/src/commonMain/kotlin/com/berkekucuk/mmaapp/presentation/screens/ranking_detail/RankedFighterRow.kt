package com.berkekucuk.mmaapp.presentation.screens.ranking_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import com.berkekucuk.mmaapp.presentation.components.FighterPortrait

@Composable
fun RankedFighterRow(
    rankLabel: String,
    name: String,
    record: String,
    imageUrl: String,
    countryCode: String? = null,
    onFighterClicked: () -> Unit
) {
    val isChampion = rankLabel == "C"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFighterClicked() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isChampion) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(AppColors.rankingChampionBadge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rankLabel,
                    color = AppColors.pagerBackground,
                    fontSize = 12.sp,
                    fontFamily = AppFonts.RobotoCondensed,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Box(
                modifier = Modifier.size(26.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rankLabel,
                    color = AppColors.textSecondary,
                    fontSize = 14.sp,
                    fontFamily = AppFonts.RobotoCondensed,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        FighterPortrait(
            name = name,
            imageUrl = imageUrl,
            countryCode = countryCode,
            result = null,
            record = record,
            alignment = Alignment.Start,
            modifier = Modifier.weight(1f),
            imageSize = 42.dp,
            flagWidth = 14.dp,
            flagHeight = 9.dp,
            nameFontSize = 14.sp,
        )
    }
}
