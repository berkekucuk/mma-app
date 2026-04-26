package com.berkekucuk.mmaapp.core.utils

import io.github.jan.supabase.postgrest.exception.PostgrestRestException

object AppErrorMapper {
    fun map(e: Throwable): AppError {
        val message = e.message?.lowercase() ?: ""
        
        return when {
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
    UNKNOWN
}
