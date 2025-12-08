package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.local.dao.EventDao
import com.berkekucuk.mmaapp.data.remote.api.EventRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock
import kotlin.time.Instant

class EventRepositoryImpl(
    private val remoteDataSource: EventRemoteDataSource,
    private val dao: EventDao
) : EventRepository {

    private val yearRangeCache = mutableMapOf<Int, Pair<Long, Long>>()

    private fun getYearRange(year: Int): Pair<Long, Long> {
        return yearRangeCache.getOrPut(year) {
            val start = LocalDateTime(year, 1, 1, 0, 0, 0).toInstant(TimeZone.UTC).toEpochMilliseconds()
            val end = LocalDateTime(year, 12, 31, 23, 59, 59).toInstant(TimeZone.UTC).toEpochMilliseconds()
            start to end
        }
    }

    override fun getEvents(): Flow<List<Event>> {
        return dao.getAllEvents()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun syncEvents(): Result<Unit> {
        return runCatching {
            val isFirstLaunch = dao.getEventCount() == 0

            if (isFirstLaunch) {
                fetchAndSaveInitialData()
            } else {
                fetchAndSavePendingEvents()
            }
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }

    override suspend fun getEventsByYear(year: Int): Result<Unit> {
        return runCatching {
            val (start, end) = getYearRange(year)
            val count = dao.getEventCountForYear(start, end)

            if (count == 0) {
                val events = remoteDataSource.fetchEventsByYear(year)
                if (events.isNotEmpty()) {
                    dao.insertEvents(events.map { it.toEntity() })
                }
            }
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }

    override suspend fun forceRefreshFeaturedTab(): Result<Unit> {
        return runCatching {
            fetchAndSaveInitialData()
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }

    override suspend fun forceRefreshUpcomingTab(): Result<Unit> {
        return runCatching {
            fetchAndSavePendingEvents()
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }

    override suspend fun forceRefreshCompletedTab(year: Int): Result<Unit> {
        return runCatching {
            fetchAndSaveCompletedEvents(year)
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }

    private suspend fun fetchAndSaveInitialData() {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val currentYear = now.year
        val currentMonth = now.month.number

        val yearsToSync = if (currentMonth < 7) {
            listOf(currentYear, currentYear - 1)
        } else {
            listOf(currentYear, currentYear + 1)
        }

        coroutineScope {
            yearsToSync.map { year ->
                async {
                    val events = remoteDataSource.fetchEventsByYear(year)
                    if (events.isNotEmpty()) {
                        dao.insertEvents(events.map { it.toEntity() })
                    }
                }
            }.forEach { it.await() }
        }
    }

    private suspend fun fetchAndSavePendingEvents() {
        val lastPendingTimestamp = dao.getOldestPendingEventDate()

        val syncAfterDate = lastPendingTimestamp?.let {
            Instant.fromEpochMilliseconds(it)
        } ?: Clock.System.now()

        val events = remoteDataSource.fetchEventsAfter(syncAfterDate)

        if (events.isNotEmpty()) {
            dao.insertEvents(events.map { it.toEntity() })
        }
    }

    private suspend fun fetchAndSaveCompletedEvents(year: Int){
        val events = remoteDataSource.fetchEventsByYear(year)
        if (events.isNotEmpty()) {
            dao.insertEvents(events.map { it.toEntity() })
        }
    }
}
