package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    rankChange: Int? = null,
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

        //Rank Change Indicator
        if (rankChange != null) {
            RankChangeIndicator(rankChange = rankChange)
        }
    }
}

@Composable
private fun RankChangeIndicator(rankChange: Int) {
    Row( verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.padding(end = 4.dp)
    ) {
        when { rankChange > 0 -> {
            Icon( imageVector = Icons.Default.ArrowDropUp,
            contentDescription = "Rank Up",
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp)
            )
            Text(text = rankChange.toString(),
            color = Color(0xFF4CAF50),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            )
        }
        rankChange < 0 -> {
            Icon( imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Rank Down",
            tint = Color(0xFFF44336),
            modifier = Modifier.size(20.dp)
            )
            Text(text = kotlin.math.abs(rankChange).toString(),
            color = Color(0xFFF44336),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            )
        }
        else -> {
            Text( text = "-",
            color = AppColors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 4.dp)
            )
        }}
    }
}
