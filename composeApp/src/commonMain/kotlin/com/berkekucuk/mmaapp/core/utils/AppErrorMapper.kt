package com.berkekucuk.mmaapp.core.utils

import io.github.jan.supabase.postgrest.exception.PostgrestRestException

object AppErrorMapper {
    fun map(e: Throwable): AppError {
        val message = e.message?.lowercase() ?: ""
        
        return when {
            message.contains("profiles_username_key") -> AppError.USERNAME_TAKEN
            message.contains("unique_user_fighter_interaction") -> AppError.ALREADY_EXISTS
            message.contains("not authenticated") || message.contains("unauthenticated") -> AppError.UNAUTHENTICATED
            
            message.contains("Odds pending. Predictions opening soon.") -> AppError.ODDS_NOT_PUBLISHED
            message.contains("Event already over.") -> AppError.EVENT_OVER
            message.contains("Fight already over.") -> AppError.FIGHT_OVER
            message.contains("Result pending. Predictions locked.") -> AppError.FIGHT_PENDING

            message.contains("network") ||
            message.contains("timeout") || 
            message.contains("connection") || 
            message.contains("host") ||
            message.contains("resolv") -> AppError.NETWORK

            e is PostgrestRestException -> AppError.SERVER_ERROR
            else -> AppError.UNKNOWN
        }
    }
}

enum class AppError {
    NETWORK,
    SERVER_ERROR,
    UNAUTHENTICATED,
    ALREADY_EXISTS,
    USERNAME_TAKEN,
    EMPTY_FULLNAME,
    FULLNAME_TOO_SHORT,
    FULLNAME_TOO_LONG,
    EMPTY_USERNAME,
    INVALID_USERNAME,
    USERNAME_TOO_SHORT,
    USERNAME_TOO_LONG,
    ODDS_NOT_PUBLISHED,
    EVENT_OVER,
    FIGHT_OVER,
    FIGHT_PENDING,
    UNKNOWN
}
