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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class EventRepositoryImpl(
    private val remoteDataSource: EventRemoteDataSource,
    private val dao: EventDao,
    private val dateTimeProvider: DateTimeProvider,
    private val rateLimiter: RateLimiter
) : EventRepository {

    private companion object {
        private const val KEY_REFRESH_PENDING_EVENTS = "refresh_pending_events"
        fun getSyncKey(year: Int) = "sync_events_$year"
        fun getRefreshEventKey(eventId: String) = "refresh_event_$eventId"
    }

    override fun getEvents(): Flow<List<Event>> {
        return dao.getAllEvents()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }

    override fun getEventById(eventId: String): Flow<Event> {
        return dao.getEventById(eventId)
            .map { it.toDomain() }
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
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

    override suspend fun refreshEvents(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(KEY_REFRESH_PENDING_EVENTS)) {
                    return@runCatching
                }
                fetchPendingEvents()
                rateLimiter.markAsFetched(KEY_REFRESH_PENDING_EVENTS)
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(KEY_REFRESH_PENDING_EVENTS)
            }
        }
    }

    override suspend fun refreshEventById(eventId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val refreshKey = getRefreshEventKey(eventId)
            runCatching {
                if (!rateLimiter.shouldFetch(refreshKey)) {
                    return@runCatching
                }
                val events = remoteDataSource.fetchEventsById(eventId)
                if (events.isNotEmpty()) {
                    dao.insertEvents(events.map { it.toEntity() })
                }
                rateLimiter.markAsFetched(refreshKey)
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(refreshKey)
            }
        }
    }

    override suspend fun getEventsByYear(year: Int, forceRefresh: Boolean): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val syncKey = getSyncKey(year)
            runCatching {
                if (!rateLimiter.shouldFetch(syncKey)) {
                    return@runCatching
                }

                if (shouldFetchFromApi(year, forceRefresh)) {
                    val events = remoteDataSource.fetchEventsByYear(year)
                    if (events.isNotEmpty()) {
                        dao.insertEvents(events.map { it.toEntity() })
                        rateLimiter.markAsFetched(syncKey)
                    }
                }
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(syncKey)
            }
        }
    }

    private suspend fun shouldFetchFromApi(year: Int, forceRefresh: Boolean): Boolean {
        if (forceRefresh) return true
        return !dao.hasEventsForYear(year)
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
                        rateLimiter.markAsFetched(getSyncKey(year))
                    }
                }
            }.forEach { it.await() }
        }
    }

    private suspend fun fetchPendingEvents() {
        val syncAfterDate = dao.getOldestPendingEventDate() ?: dateTimeProvider.now

        val events = remoteDataSource.fetchEventsAfter(syncAfterDate)

        if (events.isNotEmpty()) {
            dao.insertEvents(events.map { it.toEntity() })
        }
    }
}