package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.domain.repository.FighterRepository
import com.berkekucuk.mmaapp.domain.repository.NotificationRepository
import com.berkekucuk.mmaapp.domain.repository.PredictionRepository
import com.berkekucuk.mmaapp.core.storage.NotificationStorage
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.domain.model.Fighter
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FightDetailViewModel(
    private val eventRepository: EventRepository,
    private val fighterRepository: FighterRepository,
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository,
    private val predictionRepository: PredictionRepository,
    private val notificationStorage: NotificationStorage,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.FightDetail>()
    private val eventId: String = route.eventId
    private val fightId: String = route.fightId
    private val fighterId: String? = route.fighterId
    val cameFromEvent: Boolean = fighterId == null
    private val _state = MutableStateFlow(FightDetailUiState())
    val state = _state.asStateFlow()
    private val _navigation = MutableSharedFlow<FightDetailNavigationEvent>()
    val navigation = _navigation.asSharedFlow()
    private var isPendingNotificationRequest = false

    init {
        if (fighterId != null) {
            observeFightFromFighter()
        } else {
            observeFightFromEvent()
        }
        observeFightNotificationStatus()
        observePredictionStatus()
    }

    private fun observeFightFromEvent() {
        viewModelScope.launch {
            eventRepository.getEventById(eventId)
                .collect { event ->
                    val fight = event.fights.find { it.fightId == fightId }

                    _state.update {
                        it.copy(
                            fight = fight,
                            eventDate = event.datetimeUtc,
                            eventName = event.name,
                            showPredictionBoard = fight != null && !isFightCompleted(fight)
                        )
                    }
                    if (fight != null) {
                        syncFighters(fight)
                    }
                }
        }
    }

    private fun observeFightFromFighter() {
        viewModelScope.launch {
            fighterRepository.getFighterById(fighterId!!)
                .collect { fighter ->
                    val fight = fighter.fights.find { it.fightId == fightId }

                    _state.update {
                        it.copy(
                            fight = fight,
                            eventName = fight?.eventName,
                            eventDate = fight?.eventDate,
                            showPredictionBoard = fight != null && !isFightCompleted(fight)
                        )
                    }
                    if (fight != null) {
                        syncFighters(fight, knownFighterId = fighterId)
                    }
                }
        }
    }

    private fun observeFightNotificationStatus() {
        viewModelScope.launch {
            val userId = getAuthenticatedUserId()
            if (userId != null) {
                notificationRepository.getFightNotificationStatus(fightId, userId)
                    .collect { isEnabled ->
                        _state.update { it.copy(isNotificationEnabled = isEnabled) }
                    }
            }
        }
    }

    private fun observePredictionStatus() {
        viewModelScope.launch {
            val userId = getAuthenticatedUserId()
            if (userId != null) {
                predictionRepository.getPredictedWinnerId(fightId, userId)
                    .collect { predictedId ->
                        _state.update { it.copy(predictedWinnerId = predictedId) }
                    }
            }
        }
    }

    private fun syncFighters(fight: Fight, knownFighterId: String? = null) {
        val redId = fight.redCorner?.fighter?.fighterId ?: return
        val blueId = fight.blueCorner?.fighter?.fighterId ?: return
        _state.update { it.copy(error = null) }
        loadFighter(redId, sync = redId != knownFighterId) { fighter ->
            _state.update { it.copy(redFighter = fighter) }
        }
        loadFighter(blueId, sync = blueId != knownFighterId) {
            fighter -> _state.update { it.copy(blueFighter = fighter) }
        }
    }

    private fun loadFighter(fighterId: String, sync: Boolean, onUpdate: (Fighter) -> Unit) {
        viewModelScope.launch {
            if (sync) {
                fighterRepository.syncFighter(fighterId)
                    .onFailure { e ->
                        val errorType = when (e) {
                            is PostgrestRestException -> FightDetailError.UNKNOWN_ERROR
                            else -> FightDetailError.NETWORK_ERROR
                        }
                        _state.update { it.copy(error = errorType) }
                    }
            }

            val fighter = fighterRepository.getFighterById(fighterId).first()
            onUpdate(fighter)
        }
    }

    fun onAction(action: FightDetailUiAction) {
        when (action) {
            is FightDetailUiAction.OnFighterClicked -> {
                if (action.fighterId == fighterId) {
                    navigateTo(FightDetailNavigationEvent.Back)
                } else {
                    navigateTo(FightDetailNavigationEvent.ToFighterDetail(action.fighterId))
                }
            }
            is FightDetailUiAction.OnBackClicked -> navigateTo(FightDetailNavigationEvent.Back)
            is FightDetailUiAction.OnRefresh -> onRefresh()
            is FightDetailUiAction.OnEventClicked -> navigateTo(FightDetailNavigationEvent.ToEventDetail(action.eventId))
            is FightDetailUiAction.OnNotificationClicked -> onNotificationClicked()
            is FightDetailUiAction.OnErrorShown -> _state.update { it.copy(error = null) }
            is FightDetailUiAction.OnResume -> {
                if (isPendingNotificationRequest && notificationStorage.load()) {
                    isPendingNotificationRequest = false
                    onNotificationClicked()
                }
            }
            is FightDetailUiAction.OnSubmitPredictionClicked -> submitPrediction(action.predictedWinnerId)
        }
    }

    private fun onNotificationClicked() {
        viewModelScope.launch {
            val userId = getAuthenticatedUserId()
            if (userId == null) {
                _state.update { it.copy(error = FightDetailError.NOT_AUTHENTICATED) }
                return@launch
            }

            val fight = _state.value.fight ?: return@launch
            val isNotificationEnabled = _state.value.isNotificationEnabled
            if (!canToggleNotification(fight, isNotificationEnabled)) return@launch

            if (isNotificationEnabled) {
                removeNotification(fight.fightId, userId)
            } else {
                enableNotificationWithPermissionCheck(fight.fightId, userId)
            }
        }
    }

    private fun isFightCompleted(fight: Fight): Boolean {
        return fight.methodType.isNotBlank() || fight.methodDetail.isNotBlank()
    }

    private fun canToggleNotification(fight: Fight, isNotificationEnabled: Boolean): Boolean {
        if (isFightCompleted(fight) && !isNotificationEnabled) {
            _state.update { it.copy(error = FightDetailError.FIGHT_COMPLETED) }
            return false
        }
        return true
    }

    private suspend fun removeNotification(fightId: String, userId: String) {
        notificationRepository.removeFightNotification(fightId, userId)
            .onFailure {
                _state.update { it.copy(error = FightDetailError.NETWORK_ERROR) }
            }
    }

    private suspend fun enableNotificationWithPermissionCheck(fightId: String, userId: String) {
        if (!notificationStorage.load()) {
            handleMissingPermission()
            return
        }

        notificationRepository.addFightNotification(fightId, userId)
            .onFailure {
            _state.update { it.copy(error = FightDetailError.NETWORK_ERROR) }
        }
    }

    private fun handleMissingPermission() {
        isPendingNotificationRequest = true
        if (notificationStorage.hasRequestedPermission()) {
            notificationStorage.openNotificationSettings()
        } else {
            notificationStorage.setRequestedPermission(true)
            navigateTo(FightDetailNavigationEvent.RequestNotificationPermission)
        }
    }

    private fun submitPrediction(predictedWinnerId: String) {
        viewModelScope.launch {
            val userId = getAuthenticatedUserId()
            if (userId == null) {
                _state.update { it.copy(error = FightDetailError.NOT_AUTHENTICATED) }
                return@launch
            }

            val fight = _state.value.fight ?: return@launch
            if (isFightCompleted(fight)) {
                _state.update { it.copy(error = FightDetailError.FIGHT_COMPLETED) }
                return@launch
            }

            if(!areOddsPublished(fight)){
                _state.update { it.copy(error = FightDetailError.ODDS_NOT_PUBLISHED) }
                return@launch
            }

            val lockedOdds = getLockedOdds(fight, predictedWinnerId)

            _state.update { it.copy(isSubmittingPrediction = true, error = null) }
            
            predictionRepository.submitPrediction(userId, fight.fightId, predictedWinnerId, lockedOdds)
                .onSuccess {
                    _state.update { it.copy(isSubmittingPrediction = false) }
                }
                .onFailure { e ->
                    val error = mapRemoteError(e)
                    _state.update { it.copy(isSubmittingPrediction = false, error = error) }
                }
        }
    }

    private fun mapRemoteError(e: Throwable): FightDetailError {
        val message = e.message ?: ""
        return when {
            message.contains("Odds not published yet. Predictions locked.", ignoreCase = true) -> FightDetailError.ODDS_NOT_PUBLISHED
            message.contains("Event already completed or cancelled.", ignoreCase = true) -> FightDetailError.EVENT_COMPLETED_OR_CANCELLED
            message.contains("Fight already over.", ignoreCase = true) -> FightDetailError.FIGHT_COMPLETED
            message.contains("Starting soon or in progress. Predictions locked.", ignoreCase = true) -> FightDetailError.FIGHT_PENDING
            e is PostgrestRestException -> FightDetailError.UNKNOWN_ERROR
            else -> FightDetailError.NETWORK_ERROR
        }
    }

    private fun areOddsPublished(fight: Fight): Boolean {
        return fight.participants.all { it.oddsValue != null && it.oddsValue != 0 }
    }

    private fun getLockedOdds(fight: Fight, predictedWinnerId: String): Int {
        return fight.participants.find { it.fighter.fighterId == predictedWinnerId }?.oddsValue ?: 0
    }

    private suspend fun getAuthenticatedUserId(): String? {
        val authState = authRepository.authState.first { it !is AuthState.Loading }
        return if (authState is AuthState.Authenticated) authState.userId else null
    }

    private fun onRefresh() {
        if (_state.value.isRefreshing) return

        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }

            val result = if (fighterId != null) {
                fighterRepository.syncFighter(fighterId)
            } else {
                eventRepository.syncEventById(eventId = eventId)
            }

            result.onSuccess {
                _state.value.fight?.let { currentFight ->
                    syncFighters(currentFight, knownFighterId = fighterId)
                }
                _state.update { it.copy(isRefreshing = false) }
            }.onFailure { e ->
                val errorType = when (e) {
                    is PostgrestRestException -> FightDetailError.UNKNOWN_ERROR
                    else -> FightDetailError.NETWORK_ERROR
                }
                _state.update { it.copy(isRefreshing = false, error = errorType) }
            }
        }
    }

    private fun navigateTo(event: FightDetailNavigationEvent) {
        viewModelScope.launch {
            _navigation.emit(event)
        }
    }
}