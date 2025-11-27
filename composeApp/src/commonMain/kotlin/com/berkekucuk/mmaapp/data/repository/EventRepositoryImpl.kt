package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.local.dao.EventDao
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.data.remote.api.EventRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.time.Instant

class EventRepositoryImpl(
    private val remoteDataSource: EventRemoteDataSource,
    private val dao: EventDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : EventRepository {

    override fun getEvents(): Flow<List<Event>> {
        return dao.getAllEvents()
            .map { entities ->
                // Mapping process might be CPU intensive, use IO dispatcher
                entities.map { it.toDomain() }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun syncEvents(): Result<Unit>{
        return withContext(ioDispatcher) {
            try {
                // sync strategy
                val fetchFromDate = calculateFetchDate()

                // API request
                val remoteEvents = remoteDataSource.fetchEvents(afterDate = fetchFromDate)

                if (remoteEvents.isNotEmpty()) {
                    val entities = remoteEvents.map { it.toEntity() }
                    dao.insertEvents(entities)
                }

                Result.success(Unit)

            } catch (e: Exception) {
                // If the coroutine is canceled, it should not disrupt the flow instead of returning Result.failure.
                if (e is kotlinx.coroutines.CancellationException) throw e
                Result.failure(e)
            }
        }
    }
    private suspend fun calculateFetchDate(): Instant? {
        val dbCount = dao.getEventCount()

        if (dbCount == 0) return null

        return dao.getOldestPendingEventDate()?.let { timestamp ->
            Instant.fromEpochMilliseconds(timestamp)
        }
    }
}