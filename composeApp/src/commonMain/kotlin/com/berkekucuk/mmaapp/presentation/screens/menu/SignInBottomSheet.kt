package com.berkekucuk.mmaapp.presentation.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings
import com.berkekucuk.mmaapp.presentation.components.GoogleSignInButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInBottomSheet(
    onDismiss: () -> Unit,
    onStartSignIn: () -> Unit,
) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.dropdownMenuBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = strings.menuSignInButton,
                color = colors.textPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            GoogleSignInButton(
                onClick = onStartSignIn,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
