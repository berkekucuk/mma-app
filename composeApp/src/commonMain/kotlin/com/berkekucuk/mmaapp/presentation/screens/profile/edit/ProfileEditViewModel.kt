package com.berkekucuk.mmaapp.presentation.screens.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.ProfileRepository
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileEditViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileEditUiState())
    val state: StateFlow<ProfileEditUiState> = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<ProfileEditNavigationEvent>()
    val navigation = _navigation.asSharedFlow()

    private val usernameRegex = "^[a-z0-9_.]+$".toRegex()
    private val knownTakenUsernames = mutableSetOf<String>()

    init {
        observeProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isInitializing = true) }

            val authState = authRepository.authState.filterIsInstance<AuthState.Authenticated>().first()
            val profile = profileRepository.getProfile(authState.userId).filterNotNull().first()

            _state.update {
                it.copy(
                    fullName = profile.fullName ?: "",
                    username = profile.username ?: "",
                    originalFullName = profile.fullName ?: "",
                    originalUsername = profile.username ?: "",
                    isInitializing = false
                )
            }
        }
    }

    private fun getFullNameError(fullName: String): ProfileEditError? {
        return when {
            fullName.isBlank() -> ProfileEditError.EMPTY_FULLNAME
            fullName.length < 3 -> ProfileEditError.FULLNAME_TOO_SHORT
            fullName.length > 50 -> ProfileEditError.FULLNAME_TOO_LONG
            else -> null
        }
    }

    private fun getUsernameError(username: String): ProfileEditError? {
        return when {
            username.isEmpty() -> ProfileEditError.EMPTY_USERNAME
            !username.matches(usernameRegex) -> ProfileEditError.INVALID_USERNAME
            username.length < 3 -> ProfileEditError.USERNAME_TOO_SHORT
            username.length > 20 -> ProfileEditError.USERNAME_TOO_LONG
            else -> null
        }
    }

    fun onAction(action: ProfileEditUiAction) {
        when (action) {
            is ProfileEditUiAction.OnFullNameChanged -> {
                val newValue = action.value
                _state.update { it.copy(fullName = newValue, error = getFullNameError(newValue)) }
            }

            is ProfileEditUiAction.OnUsernameChanged -> {
                val newValue = action.value.lowercase()
                _state.update { it.copy(username = newValue, error = getUsernameError(newValue)) }
            }

            is ProfileEditUiAction.OnSaveClicked -> validateAndSave()
            is ProfileEditUiAction.OnBackClicked -> navigateTo(ProfileEditNavigationEvent.Back)
        }
    }

    private fun validateAndSave() {
        val currentState = _state.value

        if (currentState.isSaving) return

        val username = currentState.username.trim()
        val fullName = currentState.fullName.trim()
        val finalError = getUsernameError(username) ?: getFullNameError(fullName)
        if (finalError != null) {
            _state.update { it.copy(error = finalError) }
            return
        }

        if (username == currentState.originalUsername && fullName == currentState.originalFullName) {
            navigateTo(ProfileEditNavigationEvent.Back)
            return
        }

        if (knownTakenUsernames.contains(username)) {
            _state.update { it.copy(error = ProfileEditError.USERNAME_TAKEN) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val authState = authRepository.authState.first { it !is AuthState.Loading }

            if (authState is AuthState.Authenticated) {
                val result = profileRepository.updateProfile(
                    userId = authState.userId,
                    fullName = fullName,
                    username = username
                )

                result.onSuccess {
                    _state.update { it.copy(isSaving = false) }
                    navigateTo(ProfileEditNavigationEvent.Back)
                }.onFailure { e ->
                    val errorType = when (e) {
                        is PostgrestRestException -> {
                            if (e.code == "23505") {
                                knownTakenUsernames.add(username)
                                ProfileEditError.USERNAME_TAKEN
                            }
                            else ProfileEditError.UNKNOWN_ERROR
                        }
                        else -> ProfileEditError.NETWORK_ERROR
                    }
                    _state.update { it.copy(isSaving = false, error = errorType) }
                }
            }else {
                _state.update { it.copy(isSaving = false, error = ProfileEditError.UNKNOWN_ERROR) }
            }
        }
    }

    private fun navigateTo(event: ProfileEditNavigationEvent) {
        viewModelScope.launch { _navigation.emit(event) }
    }
}