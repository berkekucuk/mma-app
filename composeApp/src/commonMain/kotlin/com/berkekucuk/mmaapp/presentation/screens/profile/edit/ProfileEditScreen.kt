package com.berkekucuk.mmaapp.presentation.screens.profile.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppTheme
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.presentation.components.ErrorBox
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileEditScreenRoot(
    viewModel: ProfileEditViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is ProfileEditNavigationEvent.Back -> onBackClick()
            }
        }
    }

    ProfileEditScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    state: ProfileEditUiState,
    onAction: (ProfileEditUiAction) -> Unit,
) {
    val onBackClicked = remember(onAction) { { onAction(ProfileEditUiAction.OnBackClicked) } }
    val onFullNameChanged = remember(onAction) { { name: String -> onAction(ProfileEditUiAction.OnFullNameChanged(name)) } }
    val onUsernameChanged = remember(onAction) { { username: String -> onAction(ProfileEditUiAction.OnUsernameChanged(username)) } }
    val onSaveClicked = remember(onAction) { { onAction(ProfileEditUiAction.OnSaveClicked) } }

    val strings = LocalAppStrings.current
    val errorMessage = when (state.error) {
        ProfileEditError.NETWORK_ERROR -> strings.errorNetwork
        ProfileEditError.USERNAME_TAKEN -> strings.profileEditErrorUsernameTaken
        ProfileEditError.EMPTY_USERNAME -> strings.profileEditErrorEmptyUsername
        ProfileEditError.INVALID_USERNAME -> strings.profileEditErrorInvalidUsername
        ProfileEditError.USERNAME_TOO_SHORT -> strings.profileEditErrorUsernameShort
        ProfileEditError.USERNAME_TOO_LONG -> strings.profileEditErrorUsernameLong
        ProfileEditError.EMPTY_FULLNAME -> strings.profileEditErrorEmptyFullname
        ProfileEditError.FULLNAME_TOO_SHORT -> strings.profileEditErrorFullnameShort
        ProfileEditError.FULLNAME_TOO_LONG -> strings.profileEditErrorFullnameLong
        ProfileEditError.UNKNOWN_ERROR -> strings.errorUnknown
        null -> null
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = AppTheme.colors.pagerBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = strings.profileEditTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = strings.contentDescriptionBack,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.topBarBackground,
                    scrolledContainerColor = AppTheme.colors.topBarBackground,
                    navigationIconContentColor = AppTheme.colors.textPrimary,
                    titleContentColor = AppTheme.colors.textPrimary,
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = strings.profileEditPersonalInfo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.textSecondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = state.fullName,
                onValueChange = onFullNameChanged,
                textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
                label = { Text(strings.profileEditFullName, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppTheme.colors.textPrimary,
                    unfocusedTextColor = AppTheme.colors.textPrimary,
                    errorTextColor = AppTheme.colors.textPrimary,
                    focusedBorderColor = AppTheme.colors.winnerFrame,
                    unfocusedBorderColor = AppTheme.colors.cardBorder,
                    errorBorderColor = AppTheme.colors.ufcRed,
                    focusedLabelColor = AppTheme.colors.textPrimary,
                    unfocusedLabelColor = AppTheme.colors.textSecondary,
                    errorLabelColor = AppTheme.colors.ufcRed,
                    cursorColor = AppTheme.colors.textPrimary,
                    errorCursorColor = AppTheme.colors.ufcRed
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.username,
                onValueChange = onUsernameChanged,
                textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
                label = { Text(strings.profileEditUsernameLabel, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppTheme.colors.textPrimary,
                    unfocusedTextColor = AppTheme.colors.textPrimary,
                    errorTextColor = AppTheme.colors.textPrimary,
                    focusedBorderColor = AppTheme.colors.winnerFrame,
                    unfocusedBorderColor = AppTheme.colors.cardBorder,
                    errorBorderColor = AppTheme.colors.ufcRed,
                    focusedLabelColor = AppTheme.colors.textPrimary,
                    unfocusedLabelColor = AppTheme.colors.textSecondary,
                    errorLabelColor = AppTheme.colors.ufcRed,
                    cursorColor = AppTheme.colors.textPrimary,
                    errorCursorColor = AppTheme.colors.ufcRed
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(24.dp))
                ErrorBox(message = errorMessage)
            }

            Spacer(modifier = Modifier.weight(1f))

            SaveButton(
                text = strings.profileEditSaveChanges,
                onClick = onSaveClicked,
                isSaving = state.isSaving,
            )
        }
    }
}