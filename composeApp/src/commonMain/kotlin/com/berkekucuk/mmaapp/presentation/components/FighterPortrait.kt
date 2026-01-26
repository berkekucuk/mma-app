package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FighterPortrait(
    name: String?,
    imageUrl: String?,
    countryCode: String?,
    result: String?,
    record: String?,
    alignment: Alignment.Horizontal,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = if (alignment == Alignment.Start) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
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
