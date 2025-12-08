package com.berkekucuk.mmaapp.utils

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

    // Format: "07 Feb 2026 23:00"
    val day = localDateTime.day.toString().padStart(2, '0')
    val month = months[localDateTime.month.number - 1]
    val year = localDateTime.year
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')

    return "$day $month $year $hour:$minute"
}
