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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.LocalPlatformContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.berkekucuk.mmaapp.core.presentation.AppColors

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
    val context = LocalPlatformContext.current

    Box(
        contentAlignment = if (alignment == Alignment.Start) Alignment.BottomStart else Alignment.BottomEnd
    ) {
        val isWinner = result?.equals("WIN", ignoreCase = true) == true
        val borderModifier = if (isWinner) {
            Modifier.border(2.dp, AppColors.winner, CircleShape)
        } else {
            Modifier
        }

        val imageRequest = remember(imageUrl) {
            ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .size(200, 200)
                .memoryCacheKey(imageUrl)
                .build()
        }

        AsyncImage(
            model = imageRequest,
            contentDescription = name,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(AppColors.TopBarBackground)
                .then(borderModifier),
            contentScale = ContentScale.Crop
        )

        countryCode?.let { code ->
            val flagUrl = "https://ictgktsdedzcydjwhosx.supabase.co/storage/v1/object/public/flags/${code.lowercase()}.png"

            val flagRequest = remember(flagUrl) {
                ImageRequest.Builder(context)
                    .data(flagUrl)
                    .crossfade(true)
                    .size(100, 100)
                    .memoryCacheKey(flagUrl)
                    .build()
            }

            AsyncImage(
                model = flagRequest,
                contentDescription = "Flag",
                modifier = Modifier
                    .size(width = 18.dp, height = 12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(0.5.dp, AppColors.CardBackground, RoundedCornerShape(2.dp)),
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
    val nameParts = remember(name) {
        name?.split(" ") ?: listOf("Unknown", "Fighter")
    }

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
