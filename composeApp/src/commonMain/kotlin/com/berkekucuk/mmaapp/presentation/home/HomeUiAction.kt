package com.berkekucuk.mmaapp.presentation.home

// 🔁 UI Intent (MVI Eventler)
sealed interface HomeUiAction {
    data class OnFightClicked(val fightId: String) : HomeUiAction
    data class OnPageChanged(val index: Int) : HomeUiAction
}

// UI bu event’leri ViewModel’e gönderir
// ViewModel state günceller veya NavigationEvent üretir
