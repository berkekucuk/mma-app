package com.berkekucuk.mmaapp.domain.enums

enum class ReportReason {
    INAPPROPRIATE_PROFILE_PICTURE,
    INAPPROPRIATE_USERNAME,
    SPAM_OR_BOT,
    ABUSIVE_OR_HARASSING_BEHAVIOR,
    OTHER;

    val dbValue: String
        get() = when (this) {
            INAPPROPRIATE_PROFILE_PICTURE -> "Inappropriate profile picture"
            INAPPROPRIATE_USERNAME -> "Inappropriate username"
            SPAM_OR_BOT -> "Spam or bot"
            ABUSIVE_OR_HARASSING_BEHAVIOR -> "Abusive or harassing behavior"
            OTHER -> "Other"
        }
}