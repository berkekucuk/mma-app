package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings

@Composable
fun NameColumn(
    name: String,
    result: String?,
    record: String?,
    textAlign: TextAlign,
    horizontalAlignment: Alignment.Horizontal,
    modifier: Modifier = Modifier,
    nameFontSize: TextUnit = 14.sp,
    nameTrailingContent: @Composable (() -> Unit)? = null,
) {
    val strings = LocalAppStrings.current
    val winContentDescription = strings.contentDescriptionWin
    val lossContentDescription = strings.contentDescriptionLoss

    val textMeasurer = rememberTextMeasurer()
    val nameStyle = TextStyle(fontSize = nameFontSize)
    val density = LocalDensity.current

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

    BoxWithConstraints(modifier = modifier) {
        val maxWidthPx = with(density) { maxWidth.toPx() }

        val (firstName, lastName) = remember(name, maxWidthPx) {
            val parts = name.split(" ")
            val textWidth = textMeasurer.measure(name, nameStyle).size.width
            if (textWidth > maxWidthPx && parts.size > 1) {
                parts.first() to parts.drop(1).joinToString(" ")
            } else {
                name to null
            }
        }

        Column(
            horizontalAlignment = horizontalAlignment,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (lastName != null) {
                Text(
                    text = firstName,
                    color = nameColor,
                    fontSize = nameFontSize,
                    fontWeight = FontWeight.Medium,
                    textAlign = textAlign,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = lastName,
                        color = nameColor,
                        fontSize = nameFontSize,
                        fontWeight = FontWeight.Medium,
                        textAlign = textAlign,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    nameTrailingContent?.invoke()
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = firstName,
                        color = nameColor,
                        fontSize = nameFontSize,
                        fontWeight = FontWeight.Medium,
                        textAlign = textAlign,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    nameTrailingContent?.invoke()
                }
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
}