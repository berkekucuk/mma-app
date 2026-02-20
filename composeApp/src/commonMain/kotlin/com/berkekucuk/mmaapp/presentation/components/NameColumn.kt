package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.content_description_loss
import mmaapp.composeapp.generated.resources.content_description_win
import mmaapp.composeapp.generated.resources.unknown_fighter
import org.jetbrains.compose.resources.stringResource

@Composable
fun NameColumn(
    name: String?,
    result: String?,
    record: String?,
    textAlign: TextAlign,
    horizontalAlignment: Alignment.Horizontal,
    modifier: Modifier = Modifier,
) {
    val unknownFighter = stringResource(Res.string.unknown_fighter)
    val winContentDescription = stringResource(Res.string.content_description_win)
    val lossContentDescription = stringResource(Res.string.content_description_loss)

    val fullName = name ?: unknownFighter

    val (firstName, lastName) = remember(fullName) {
        val parts = fullName.split(" ")
        if (fullName.length > 14 && parts.size > 1) {
            parts.first() to parts.drop(1).joinToString(" ")
        } else {
            fullName to null
        }
    }

    val resultUpper = result?.uppercase()

    val nameColor = when (resultUpper) {
        "WIN" -> AppColors.textPrimary
        "LOSS" -> AppColors.textSecondary
        else -> AppColors.textPrimary
    }

    val recordColor: Color = when (resultUpper) {
        "WIN" -> AppColors.winnerFrame
        else -> AppColors.textSecondary
    }

    val recordIcon: ImageVector? = when (resultUpper) {
        "WIN" -> Icons.Default.ArrowDropUp
        "LOSS" -> Icons.Default.ArrowDropDown
        else -> null
    }

    val recordIconContentDescription: String? = when (resultUpper) {
        "WIN" -> winContentDescription
        "LOSS" -> lossContentDescription
        else -> null
    }

    val recordRowArrangement = if (horizontalAlignment == Alignment.End) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier,
    ) {
        Text(
            text = firstName,
            color = nameColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = textAlign,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (lastName != null) {
            Text(
                text = lastName,
                color = nameColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = textAlign,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (record != null) {
            Row(
                horizontalArrangement = recordRowArrangement,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (recordIcon != null && recordIconContentDescription != null) {
                    Icon(
                        imageVector = recordIcon,
                        contentDescription = recordIconContentDescription,
                        tint = recordColor,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Text(
                    text = record,
                    color = recordColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = textAlign,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}