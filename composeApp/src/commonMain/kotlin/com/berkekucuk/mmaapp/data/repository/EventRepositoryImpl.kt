package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.core.utils.DateTimeProvider
import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.data.local.dao.EventDao
import com.berkekucuk.mmaapp.data.remote.api.EventRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.data.local.entity.SyncedYearEntity
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.filterNotNull

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

    override fun getUpcomingEvents(): Flow<List<Event>> {
        return dao.getUpcomingEvents()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }

    override fun getCompletedEventsByYear(year: Int): Flow<List<Event>> {
        return dao.getCompletedEventsByYear(year)
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }

    override fun getEventById(eventId: String): Flow<Event> {
        return dao.getEventById(eventId)
            .filterNotNull()
            .map { it.toDomain() }
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }

    override suspend fun initializeEvents(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val currentYear = dateTimeProvider.currentYear
            if (needsInitialSync(currentYear)) {
                syncEventsByYear(currentYear)
            } else {
                syncPendingEvents()
            }
        }
    }

    override suspend fun syncEventById(eventId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val refreshKey = getRefreshEventKey(eventId)
            runCatching {
                if (!rateLimiter.shouldFetch(refreshKey)) {
                    return@runCatching
                }
                val events = remoteDataSource.fetchEventById(eventId)
                if (events.isNotEmpty()) {
                    dao.insertEvents(events.map { it.toEntity() })
                }
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(refreshKey)
            }
        }
    }

    override suspend fun syncEventsByYear(year: Int, forceRefresh: Boolean): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val syncKey = getSyncKey(year)
            runCatching {
                if (!forceRefresh && dao.isYearFullySynced(year)) return@runCatching
                if (!rateLimiter.shouldFetch(syncKey)) return@runCatching
                val events = remoteDataSource.fetchEventsByYear(year)
                if (events.isNotEmpty()) {
                    dao.insertEvents(events.map { it.toEntity() })
                    dao.markYearSynced(SyncedYearEntity(year))
                }
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(syncKey)
            }
        }
    }

    override suspend fun isYearSynced(year: Int): Boolean {
        return withContext(Dispatchers.IO) { dao.isYearFullySynced(year) }
    }

    override suspend fun syncPendingEvents(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(KEY_REFRESH_PENDING_EVENTS)) return@runCatching
                val syncAfterDate = dao.getOldestPendingEventDate() ?: dateTimeProvider.now
                val events = remoteDataSource.fetchEventsAfter(syncAfterDate)
                if (events.isNotEmpty()) {
                    dao.insertEvents(events.map { it.toEntity() })
                }
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(KEY_REFRESH_PENDING_EVENTS)
            }
        }
    }

    override suspend fun hasEventById(eventId: String): Boolean {
        return withContext(Dispatchers.IO) { dao.hasEventById(eventId) }
    }

    private suspend fun needsInitialSync(year: Int): Boolean {
        return !dao.hasEventsForYear(year)
    }
}