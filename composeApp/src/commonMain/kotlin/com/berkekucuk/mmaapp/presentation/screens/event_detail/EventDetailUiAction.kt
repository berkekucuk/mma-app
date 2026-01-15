package com.berkekucuk.mmaapp.presentation.screens.event_detail

sealed class EventDetailUiAction {
    data class OnFightClicked(val fightId: String) : EventDetailUiAction()
    data object OnBackClicked : EventDetailUiAction()
    data object OnRefresh: EventDetailUiAction()
}
