package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.core.presentation.AppColors
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.weight_class_bout
import org.jetbrains.compose.resources.stringResource

@Composable
fun FightItem(
    fight: Fight,
    modifier: Modifier = Modifier
) {
    val redFighter = fight.redCorner
    val blueFighter = fight.blueCorner

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.redGradient)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val methodText = buildString {
                if (fight.method.isNotBlank()) {
                    append(fight.method)
                    if (fight.methodDetail.isNotBlank()) {
                        append(" - ${fight.methodDetail}")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.3f))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = methodText.ifEmpty {
                        stringResource(
                            Res.string.weight_class_bout,
                            fight.weightClass.name.lowercase().replaceFirstChar { it.uppercase() }
                        )
                    },
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FighterPortrait(
                    name = redFighter?.name,
                    imageUrl = redFighter?.imageUrl,
                    countryCode = redFighter?.countryCode,
                    result = redFighter?.result?.name,
                    record = redFighter?.recordAfterFight ?: redFighter?.record,
                    alignment = Alignment.Start
                )

                FighterPortrait(
                    name = blueFighter?.name,
                    imageUrl = blueFighter?.imageUrl,
                    countryCode = blueFighter?.countryCode,
                    result = blueFighter?.result?.name,
                    record = blueFighter?.recordAfterFight ?: blueFighter?.record,
                    alignment = Alignment.End
                )
            }

            Spacer(modifier = Modifier.weight(0.4f))
        }
    }
}
