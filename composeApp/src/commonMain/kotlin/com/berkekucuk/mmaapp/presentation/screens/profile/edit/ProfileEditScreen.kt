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
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
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

    val errorMessage = when (state.error) {
        ProfileEditError.NETWORK_ERROR -> stringResource(Res.string.profile_edit_error_network)
        ProfileEditError.USERNAME_TAKEN -> stringResource(Res.string.profile_edit_error_username_taken)
        ProfileEditError.EMPTY_USERNAME -> stringResource(Res.string.profile_edit_error_empty_username)
        ProfileEditError.INVALID_USERNAME -> stringResource(Res.string.profile_edit_error_invalid_username)
        ProfileEditError.USERNAME_TOO_SHORT -> stringResource(Res.string.profile_edit_error_username_short)
        ProfileEditError.USERNAME_TOO_LONG -> stringResource(Res.string.profile_edit_error_username_long)
        ProfileEditError.EMPTY_FULLNAME -> stringResource(Res.string.profile_edit_error_empty_fullname)
        ProfileEditError.FULLNAME_TOO_SHORT -> stringResource(Res.string.profile_edit_error_fullname_short)
        ProfileEditError.FULLNAME_TOO_LONG -> stringResource(Res.string.profile_edit_error_fullname_long)
        ProfileEditError.UNKNOWN_ERROR -> stringResource(Res.string.profile_edit_error_unknown)
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
                        text = stringResource(Res.string.profile_edit_title),
                        style = AppTypography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.content_description_back),
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
                        text = stringResource(Res.string.profile_edit_personal_info),
                        style = AppTypography.titleMedium,
                        color = AppColors.textSecondary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = state.fullName,
                        onValueChange = onFullNameChanged,
                        textStyle = AppTypography.bodyMedium,
                        label = { Text(stringResource(Res.string.profile_edit_full_name), style = AppTypography.bodyMedium) },
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
                        label = { Text(stringResource(Res.string.profile_edit_username_label), style = AppTypography.bodyMedium) },
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
                        text = stringResource(Res.string.profile_edit_save_changes),
                        onClick = onSaveClicked,
                        isSaving = state.isSaving,
                    )
                }
        }
    }
}