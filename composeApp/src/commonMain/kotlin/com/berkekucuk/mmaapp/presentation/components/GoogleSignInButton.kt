package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.ic_google_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(colors.white)
            .clickable { onClick() }
            .padding(horizontal = 28.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_google_logo),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = strings.menuSignInWithGoogle,
            color = colors.googleSignInButtonText,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
