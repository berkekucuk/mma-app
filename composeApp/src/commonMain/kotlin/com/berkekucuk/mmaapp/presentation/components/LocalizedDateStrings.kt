package com.berkekucuk.mmaapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.berkekucuk.mmaapp.core.presentation.AppLanguage
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings

data class LocalizedDateStrings(
    val months: List<String>,
    val daysOfWeek: List<String>,
)

private val EN_MONTHS = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
private val EN_DAYS = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
private val TR_MONTHS = listOf("Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara")
private val TR_DAYS = listOf("Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar")

@Composable
fun rememberLocalizedDateStrings(): LocalizedDateStrings {
    val language = LocalAppStrings.current.language
    return remember(language) {
        when (language) {
            AppLanguage.EN -> LocalizedDateStrings(months = EN_MONTHS, daysOfWeek = EN_DAYS)
            AppLanguage.TR -> LocalizedDateStrings(months = TR_MONTHS, daysOfWeek = TR_DAYS)
        }
    }
}
