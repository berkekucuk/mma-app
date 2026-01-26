package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    val redCorner = fight.redCorner
    val blueCorner = fight.blueCorner

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
                if (fight.methodType.isNotBlank()) {
                    append(fight.methodType)
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
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FighterPortrait(
                    name = redCorner?.fighter?.name,
                    imageUrl = redCorner?.fighter?.imageUrl,
                    countryCode = redCorner?.fighter?.countryCode,
                    result = redCorner?.result?.name,
                    record = redCorner?.recordAfterFight?.toString() ?: redCorner?.fighter?.record?.toString(),
                    alignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier.height(55.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VS",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                FighterPortrait(
                    name = blueCorner?.fighter?.name,
                    imageUrl = blueCorner?.fighter?.imageUrl,
                    countryCode = blueCorner?.fighter?.countryCode,
                    result = blueCorner?.result?.name,
                    record = blueCorner?.recordAfterFight?.toString() ?: blueCorner?.fighter?.record?.toString(),
                    alignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(0.4f))
        }
    }
}
