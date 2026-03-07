package com.berkekucuk.mmaapp.core.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

private val MONTHS = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

private val Instant.localDateTime get() = toLocalDateTime(TimeZone.currentSystemDefault())

fun Instant.toUserFriendlyDate(): String {
    val localDateTime = this.localDateTime
    val day = localDateTime.day.toString().padStart(2, '0')
    val month = MONTHS[localDateTime.month.number - 1]
    val year = localDateTime.year
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')
    return "$day $month $year | $hour:$minute"
}

fun Instant.toYear(): String = localDateTime.year.toString()

fun Instant.toShortDate(): String {
    val ldt = localDateTime
    return "${MONTHS[ldt.month.number - 1]} ${ldt.day}"
}

fun calculateAgeAtDate(dateOfBirth: String?, referenceDate: Instant?): String? {
    if (dateOfBirth.isNullOrBlank() || referenceDate == null) return null
    return try {
        val birth = LocalDate.parse(dateOfBirth)
        val refDate = referenceDate.toLocalDateTime(TimeZone.UTC).date
        var age = refDate.year - birth.year
        if (refDate.month < birth.month ||
            (refDate.month == birth.month && refDate.day < birth.day)
        ) {
            age--
        }
        age.toString()
    } catch (e: Exception) {
        null
    }
}

fun formatDateOfBirth(dateOfBirth: String?): String? {
    if (dateOfBirth.isNullOrBlank()) return null
    return try {
        val date = LocalDate.parse(dateOfBirth)
        val day = date.day.toString().padStart(2, '0')
        val month = MONTHS[date.month.number - 1]
        val year = date.year
        "$day $month $year"
    } catch (e: Exception) {
        null
    }
}
