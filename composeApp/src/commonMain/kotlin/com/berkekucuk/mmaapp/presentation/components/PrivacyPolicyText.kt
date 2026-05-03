package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings

@Composable
fun PrivacyPolicyText(modifier: Modifier = Modifier) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current

    val termsText = strings.menuSignInTerms
    val privacyStr = strings.menuSignInPrivacyPolicy

    val annotatedString = buildAnnotatedString {
        val startIndex = termsText.indexOf(privacyStr)

        if (startIndex >= 0) {
            val endIndex = startIndex + privacyStr.length
            append(termsText.substring(0, startIndex))

            pushLink(
                LinkAnnotation.Url(
                    url = "https://www.termsfeed.com/live/0be34d50-def3-48ea-b982-75635a10803b"
                )
            )
            withStyle(
                style = SpanStyle(
                    color = colors.textPrimary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(termsText.substring(startIndex, endIndex))
            }
            pop()

            append(termsText.substring(endIndex))
        } else {
            append(termsText)
        }
    }

    Text(
        text = annotatedString,
        color = colors.textSecondary,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}
