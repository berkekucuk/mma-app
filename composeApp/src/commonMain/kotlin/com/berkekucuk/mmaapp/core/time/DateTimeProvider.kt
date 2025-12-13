package com.berkekucuk.mmaapp.core.time

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

interface DateTimeProvider {
    val timeZone: TimeZone
    val now: Instant
    val currentYear: Int
    val currentMonth: Int
}

class SystemDateTimeProvider : DateTimeProvider {

    override val timeZone: TimeZone
        get() = TimeZone.currentSystemDefault()

    override val now: Instant
        get() = Clock.System.now()

    override val currentYear: Int
        get() = now.toLocalDateTime(timeZone).year

    override val currentMonth: Int
        get() = now.toLocalDateTime(timeZone).month.number
}
