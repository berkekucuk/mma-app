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
    modifier: Modifier = Modifier
) {
    val unknownFighter = stringResource(Res.string.unknown_fighter)
    val fullName = name ?: unknownFighter

    val (firstName, lastName) = remember(fullName) {
        val parts = fullName.split(" ")
        if (fullName.length > 14 && parts.size > 1) {
            val first = parts.first()
            val last = parts.drop(1).joinToString(" ")
            first to last
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

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        Text(
            text = firstName,
            color = nameColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = textAlign,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (lastName != null) {
            Text(
                text = lastName,
                color = nameColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = textAlign,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        record?.let { rec ->
            val resultUpper = result?.uppercase()

            Row(
                horizontalArrangement = if (horizontalAlignment == Alignment.End) Arrangement.End else Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (resultUpper) {
                    "WIN" -> {
                        Icon(
                            imageVector = Icons.Default.ArrowDropUp,
                            contentDescription = stringResource(Res.string.content_description_win),
                            tint = AppColors.winnerFrame,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = rec,
                            color = AppColors.winnerFrame,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = textAlign,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    "LOSS" -> {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = stringResource(Res.string.content_description_loss),
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = rec,
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = textAlign,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    else -> {
                        Text(
                            text = rec,
                            color = AppColors.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = textAlign,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}