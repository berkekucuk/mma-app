package com.berkekucuk.mmaapp.presentation.screens.profile.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppTypography
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.presentation.components.ErrorBox
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
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
        containerColor = AppColors.pagerBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = strings.profileEditTitle,
                        style = AppTypography.titleLarge,
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
                    containerColor = AppColors.topBarBackground,
                    scrolledContainerColor = AppColors.topBarBackground,
                    navigationIconContentColor = AppColors.textPrimary,
                    titleContentColor = AppColors.textPrimary,
                )
            )
        },
    ) { innerPadding ->
        LoadingContent(
            isLoading = state.isInitializing,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Text(
                        text = strings.profileEditPersonalInfo,
                        style = AppTypography.titleMedium,
                        color = AppColors.textSecondary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = state.fullName,
                        onValueChange = onFullNameChanged,
                        textStyle = AppTypography.bodyMedium,
                        label = { Text(strings.profileEditFullName, style = AppTypography.bodyMedium) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AppColors.textPrimary,
                            unfocusedTextColor = AppColors.textPrimary,
                            errorTextColor = AppColors.textPrimary,
                            focusedBorderColor = AppColors.winnerFrame,
                            unfocusedBorderColor = AppColors.cardBorder,
                            errorBorderColor = AppColors.ufcRed,
                            focusedLabelColor = AppColors.textPrimary,
                            unfocusedLabelColor = AppColors.textSecondary,
                            errorLabelColor = AppColors.ufcRed,
                            cursorColor = AppColors.textPrimary,
                            errorCursorColor = AppColors.ufcRed
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.username,
                        onValueChange = onUsernameChanged,
                        textStyle = AppTypography.bodyMedium,
                        label = { Text(strings.profileEditUsernameLabel, style = AppTypography.bodyMedium) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AppColors.textPrimary,
                            unfocusedTextColor = AppColors.textPrimary,
                            errorTextColor = AppColors.textPrimary,
                            focusedBorderColor = AppColors.winnerFrame,
                            unfocusedBorderColor = AppColors.cardBorder,
                            errorBorderColor = AppColors.ufcRed,
                            focusedLabelColor = AppColors.textPrimary,
                            unfocusedLabelColor = AppColors.textSecondary,
                            errorLabelColor = AppColors.ufcRed,
                            cursorColor = AppColors.textPrimary,
                            errorCursorColor = AppColors.ufcRed
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
}