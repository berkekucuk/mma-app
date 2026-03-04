package com.berkekucuk.mmaapp.domain.enums

enum class EventStatus {
    UPCOMING,
    LIVE,
    COMPLETED,
    CANCELLED,
    UNKNOWN;

    companion object {
        fun fromString(status: String?): EventStatus {
            return when (status?.lowercase()) {
                "upcoming" -> UPCOMING
                "live" -> LIVE
                "completed" -> COMPLETED
                "cancelled" -> CANCELLED
                else -> UNKNOWN
            }
        }
    }
}
