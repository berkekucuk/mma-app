package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import com.berkekucuk.mmaapp.domain.model.RankedFighter
import com.berkekucuk.mmaapp.presentation.components.FighterPortrait

@Composable
fun WeightClassCard(
    weightClassName: String,
    champion: RankedFighter?,
    onWeightClassClicked: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.fightItemBackground)
            .clickable { onWeightClassClicked() }
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(3.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(AppColors.rankingWeightClassAccent)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = weightClassName.uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.textSecondary,
                )

                if (champion?.fighter != null) {
                    Spacer(Modifier.height(10.dp))

                    FighterPortrait(
                        name = champion.fighter.name,
                        imageUrl = champion.fighter.imageUrl,
                        countryCode = champion.fighter.countryCode,
                        record = champion.fighter.record.toString(),
                        alignment = Alignment.Start,
                        nameFontSize = 14.sp,
                        nameTrailingContent = if (champion.isChampion) {
                            {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AppColors.rankingChampionBadge)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "C",
                                        fontSize = 10.sp,
                                        fontFamily = AppFonts.RobotoCondensed,
                                        fontWeight = FontWeight.Bold,
                                        color = AppColors.pagerBackground
                                    )
                                }
                            }
                        } else null,
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = AppColors.textSecondary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}