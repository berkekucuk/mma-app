package com.berkekucuk.mmaapp.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.berkekucuk.mmaapp.core.presentation.AppColors

@Composable
fun LoginBackground(
    content: @Composable BoxScope.() -> Unit
) {
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            AppColors.loginGradientTop,
            AppColors.loginGradientMid1,
            AppColors.loginGradientMid2,
            AppColors.loginGradientBottom
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AppColors.ufcRed.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.5f, 0f),
                        radius = size.width * 0.9f
                    ),
                    radius = size.width * 0.9f,
                    center = Offset(size.width * 0.5f, 0f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AppColors.loginGlowWarm.copy(alpha = 0.4f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.9f, size.height * 0.95f),
                        radius = size.width * 0.6f
                    ),
                    radius = size.width * 0.6f,
                    center = Offset(size.width * 0.9f, size.height * 0.95f)
                )
            },
        content = content
    )
}
