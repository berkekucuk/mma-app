package com.berkekucuk.mmaapp.presentation.screens.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    supabaseClient: SupabaseClient = koinInject()
) {
    val action = supabaseClient.composeAuth.rememberSignInWithGoogle(
        onResult = {},
        fallback = {}
    )

    val titleAlpha = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(40f) }
    val contentOffsetY = remember { Animatable(60f) }

    LaunchedEffect(Unit) {
        titleAlpha.animateTo(1f, tween(800, easing = EaseOutCubic))
    }
    LaunchedEffect(Unit) {
        titleOffsetY.animateTo(0f, tween(800, easing = EaseOutCubic))
    }
    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, tween(900, delayMillis = 300, easing = EaseOutCubic))
    }
    LaunchedEffect(Unit) {
        contentOffsetY.animateTo(0f, tween(900, delayMillis = 300, easing = EaseOutCubic))
    }

    LoginBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginHeader(
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset { IntOffset(0, titleOffsetY.value.dp.roundToPx()) }
            )

            Spacer(modifier = Modifier.height(72.dp))

            GoogleSignInButton(
                onClick = {
                    try {
                        action.startFlow()
                    } catch (_: Exception) { }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(contentAlpha.value)
                    .offset { IntOffset(0, contentOffsetY.value.dp.roundToPx()) }
            )
        }
    }
}
