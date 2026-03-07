package com.berkekucuk.mmaapp.domain.enums

enum class Result {
    WIN,
    LOSS,
    DRAW,
    NO_CONTEST,
    PENDING,
    CANCELLED,
    FIZZLED,
    UNKNOWN;

    companion object {
        fun fromString(result: String?): Result {
            return when (result?.lowercase()) {
                "win" -> WIN
                "loss" -> LOSS
                "draw" -> DRAW
                "no_contest" -> NO_CONTEST
                "pending" -> PENDING
                "cancelled" -> CANCELLED
                "fizzled" -> FIZZLED
                else -> UNKNOWN
            }
        }
    }
}
