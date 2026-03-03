package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFighterClicked() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rankLabel,
            color = AppColors.textSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(28.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.width(8.dp))

        FighterPortrait(
            name = name,
            imageUrl = imageUrl,
            countryCode = countryCode,
            result = null,
            record = record,
            alignment = Alignment.Start,
            modifier = Modifier.weight(1f),
            imageSize = 40.dp,
            flagWidth = 14.dp,
            flagHeight = 9.dp,
            nameFontSize = 14.sp,
        )
    }
}
