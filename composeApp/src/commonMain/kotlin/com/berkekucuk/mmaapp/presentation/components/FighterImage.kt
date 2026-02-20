package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.berkekucuk.mmaapp.core.presentation.AppColors
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.content_description_flag
import org.jetbrains.compose.resources.stringResource

@Composable
fun FighterImage(
    imageUrl: String?,
    name: String?,
    countryCode: String?,
    result: String?,
    alignment: Alignment.Horizontal,
) {
    val context = LocalPlatformContext.current

    val boxAlignment = if (alignment == Alignment.Start) Alignment.BottomStart else Alignment.BottomEnd

    val isWinner = result?.equals("WIN", ignoreCase = true) == true
    val borderModifier = if (isWinner) Modifier.border(2.dp, AppColors.winnerFrame, CircleShape) else Modifier

    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .size(200, 200)
            .memoryCacheKey(imageUrl)
            .build()
    }

    val flagUrl = remember(countryCode) {
        countryCode?.let { "https://flags-v1.s3.eu-central-1.amazonaws.com/${it.lowercase()}.png" }
    }

    val flagRequest = remember(flagUrl) {
        flagUrl?.let {
            ImageRequest.Builder(context)
                .data(it)
                .crossfade(true)
                .size(100, 100)
                .memoryCacheKey(it)
                .build()
        }
    }

    val flagContentDescription = stringResource(Res.string.content_description_flag)

    Box(contentAlignment = boxAlignment) {
        AsyncImage(
            model = imageRequest,
            contentDescription = name,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(AppColors.topBarBackground)
                .then(borderModifier),
            contentScale = ContentScale.Crop,
        )

        if (flagRequest != null) {
            AsyncImage(
                model = flagRequest,
                contentDescription = flagContentDescription,
                modifier = Modifier
                    .size(width = 18.dp, height = 12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(0.5.dp, AppColors.dropdownMenuBackground, RoundedCornerShape(2.dp)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
