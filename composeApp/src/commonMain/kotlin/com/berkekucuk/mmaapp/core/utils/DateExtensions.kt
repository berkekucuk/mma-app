package com.berkekucuk.mmaapp.core.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun Instant.toUserFriendlyDate(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())

    val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    val day = localDateTime.day.toString().padStart(2, '0')
    val month = months[localDateTime.month.number - 1]
    val year = localDateTime.year
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')

    return "$day $month $year | $hour:$minute"
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