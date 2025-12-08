package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.berkekucuk.mmaapp.presentation.theme.AppColors

@Composable
fun FighterPortrait(
    name: String?,
    imageUrl: String?,
    countryCode: String?,
    result: String?,
    record: String?,
    alignment: Alignment.Horizontal
) {
    Row(
        horizontalArrangement = if (alignment == Alignment.Start) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(160.dp)
    ) {
        if (alignment == Alignment.End) {
            NameColumn(
                name = name,
                result = result,
                record = record,
                textAlign = TextAlign.End,
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FighterImage(imageUrl, name, countryCode, result, alignment)
        } else {
            FighterImage(imageUrl, name, countryCode, result, alignment)
            Spacer(modifier = Modifier.width(8.dp))
            NameColumn(
                name = name,
                result = result,
                record = record,
                textAlign = TextAlign.Start,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FighterImage(
    imageUrl: String?,
    name: String?,
    countryCode: String?,
    result: String?,
    alignment: Alignment.Horizontal
) {
    Box(
        contentAlignment = if (alignment == Alignment.Start) Alignment.BottomStart else Alignment.BottomEnd
    ) {
        val isWinner = result?.equals("WIN", ignoreCase = true) == true
        val borderModifier = if (isWinner) {
            Modifier.border(2.dp, AppColors.winner, CircleShape)
        } else {
            Modifier
        }

        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(AppColors.TopBarBackground)
                .then(borderModifier),
            contentScale = ContentScale.Crop
        )

        countryCode?.let { code ->
            val flagUrl = "https://flagcdn.com/w40/${code.lowercase()}.png"
            AsyncImage(
                model = flagUrl,
                contentDescription = "Flag",
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .border(1.dp, AppColors.CardBackground, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
private fun NameColumn(
    name: String?,
    result: String?,
    record: String?,
    textAlign: TextAlign,
    horizontalAlignment: Alignment.Horizontal,
    modifier: Modifier = Modifier
) {
    val nameParts = name?.split(" ") ?: listOf("Unknown", "Fighter")

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        Text(
            text = nameParts.joinToString(" "),
            color = AppColors.TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = textAlign,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

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
                            contentDescription = "Win",
                            tint = AppColors.winner,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = rec,
                            color = AppColors.winner,
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
                            contentDescription = "Loss",
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
                            color = AppColors.TextSecondary,
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
