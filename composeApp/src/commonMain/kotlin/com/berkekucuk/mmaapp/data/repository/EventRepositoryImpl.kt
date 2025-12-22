package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.core.utils.DateTimeProvider
import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.data.local.dao.EventDao
import com.berkekucuk.mmaapp.data.remote.api.EventRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Instant

class EventRepositoryImpl(
    private val remoteDataSource: EventRemoteDataSource,
    private val dao: EventDao,
    private val dateTimeProvider: DateTimeProvider,
    private val rateLimiter: RateLimiter
) : EventRepository {

    private companion object {
        const val KEY_UPCOMING_TAB = "tab_upcoming"
        const val KEY_FEATURED_TAB = "tab_featured"
        fun getYearKey(year: Int) = "year_$year"
    }

    override fun getEvents(): Flow<List<Event>> {
        return dao.getAllEvents()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncEvents(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val currentYear = dateTimeProvider.currentYear
                if (shouldFetchFromApi(currentYear, forceRefresh = false)) {
                    fetchInitialData()
                } else {
                    fetchPendingEvents()
                }
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }

    override suspend fun refreshFeaturedEventTab(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(KEY_FEATURED_TAB)) {
                    return@runCatching
                }
                fetchInitialData()
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(KEY_FEATURED_TAB)
            }
        }
    }

    override suspend fun refreshUpcomingEventsTab(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(KEY_UPCOMING_TAB)) {
                    return@runCatching
                }
                fetchPendingEvents()
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(KEY_UPCOMING_TAB)
            }
        }
    }

    override suspend fun getEventsByYear(year: Int, forceRefresh: Boolean): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val yearKey = getYearKey(year)
            runCatching {
                if (!rateLimiter.shouldFetch(yearKey)) {
                    return@runCatching
                }

                if (shouldFetchFromApi(year, forceRefresh)) {
                    val events = remoteDataSource.fetchEventsByYear(year)
                    if (events.isNotEmpty()) {
                        dao.insertEvents(events.map { it.toEntity() })
                        rateLimiter.markAsFetched(yearKey)
                    }
                }
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(yearKey)
            }
        }
    }

    private suspend fun shouldFetchFromApi(year: Int, forceRefresh: Boolean): Boolean {
        if (forceRefresh) return true

        val startInstant = LocalDateTime(year, 1, 1, 0, 0, 0)
            .toInstant(TimeZone.UTC)
            .toEpochMilliseconds()

        val endInstant = LocalDateTime(year, 12, 31, 23, 59, 59)
            .toInstant(TimeZone.UTC)
            .toEpochMilliseconds()

        return !dao.hasEventsForYear(startInstant, endInstant)
    }

    private suspend fun fetchInitialData() {
        val currentYear = dateTimeProvider.currentYear
        val currentMonth = dateTimeProvider.currentMonth

        val yearsToSync = if (currentMonth <= 6) {
            listOf(currentYear - 1, currentYear)
        } else {
            listOf(currentYear, currentYear + 1)
        }

        coroutineScope {
            yearsToSync.map { year ->
                async {
                    val events = remoteDataSource.fetchEventsByYear(year)
                    if (events.isNotEmpty()) {
                        dao.insertEvents(events.map { it.toEntity() })
                        rateLimiter.markAsFetched(getYearKey(year))
                    }
                }
            }.forEach { it.await() }
        }
    }

    private suspend fun fetchPendingEvents() {
        val lastPendingTimestamp = dao.getOldestPendingEventDate()

        val syncAfterDate = lastPendingTimestamp?.let {
            Instant.fromEpochMilliseconds(it)
        } ?: dateTimeProvider.now

        val events = remoteDataSource.fetchEventsAfter(syncAfterDate)

        if (events.isNotEmpty()) {
            dao.insertEvents(events.map { it.toEntity() })
        }
    }
}