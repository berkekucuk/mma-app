package com.berkekucuk.mmaapp.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.login_subtitle
import mmaapp.composeapp.generated.resources.login_title_app
import mmaapp.composeapp.generated.resources.login_title_mma
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    val oswald = AppFonts.Oswald

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(AppColors.ufcRed)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(Res.string.login_title_mma),
            fontFamily = oswald,
            fontWeight = FontWeight.Bold,
            fontSize = 56.sp,
            color = Color.White,
            letterSpacing = 8.sp,
            lineHeight = 56.sp
        )

        Text(
            text = stringResource(Res.string.login_title_app),
            fontFamily = oswald,
            fontWeight = FontWeight.Bold,
            fontSize = 56.sp,
            color = AppColors.ufcRed,
            letterSpacing = 8.sp,
            lineHeight = 56.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.login_subtitle),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = AppColors.loginSubtitle,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )
    }
}
