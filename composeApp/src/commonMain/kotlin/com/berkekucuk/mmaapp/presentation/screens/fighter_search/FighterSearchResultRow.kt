package com.berkekucuk.mmaapp.presentation.screens.fighter_search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.presentation.components.FighterPortrait

@Composable
fun FighterSearchResultRow(
    name: String,
    imageUrl: String,
    record: String,
    countryCode: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FighterPortrait(
            name = name,
            imageUrl = imageUrl,
            countryCode = countryCode,
            result = null,
            record = record,
            alignment = Alignment.Start,
            modifier = Modifier.weight(1f),
            imageSize = 42.dp,
            flagWidth = 14.dp,
            flagHeight = 9.dp,
            nameFontSize = 14.sp,
        )
    }
}
