package com.berkekucuk.mmaapp.presentation.screens.event_detail

sealed class EventDetailUiAction {
    data class onFightClicked(val fightId: String) : EventDetailUiAction()
    data object onRefresh: EventDetailUiAction()
}