package com.berkekucuk.mmaapp.core.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FetchRateLimiter(
    private val dateTimeProvider: DateTimeProvider,
    private val timeoutMs: Long = 10_000L
) {
    private val timestamps = mutableMapOf<String, Long>()
    private val mutex = Mutex()

    suspend fun shouldFetch(key: String): Boolean {
        return mutex.withLock {
            val lastFetch = timestamps[key] ?: 0L
            val now = dateTimeProvider.now.toEpochMilliseconds()

            if (now - lastFetch < timeoutMs) {
                false
            } else {
                timestamps[key] = now
                true
            }
        }
    }

    suspend fun markAsFetched(key: String) {
        mutex.withLock {
            timestamps[key] = dateTimeProvider.now.toEpochMilliseconds()
        }
    }

    suspend fun reset(key: String) {
        mutex.withLock {
            timestamps.remove(key)
        }
    }
}