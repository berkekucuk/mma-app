package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.data.local.dao.FightDao
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.data.remote.api.FightRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.domain.repository.FightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException


class FightRepositoryImpl(
    private val remoteDataSource: FightRemoteDataSource,
    private val dao: FightDao,
    private val rateLimiter: RateLimiter
) : FightRepository {

    override fun getFightsByEvent(eventId: String): Flow<List<Fight>> {
        return dao.getFightsByEvent(eventId)
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncFights(
        eventId: String,
        eventStatus: String,
        forceRefresh: Boolean
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (shouldFetchFromApi(eventId, eventStatus, forceRefresh)) {
                    val fights = remoteDataSource.fetchFightsByEvent(eventId)
                    if (fights.isNotEmpty()) {
                        dao.insertFights(fights.map { it.toEntity() })
                    }
                }
            }.onFailure { error ->
                if (error is CancellationException) throw error
                rateLimiter.reset(eventId)
            }
        }
    }

    private suspend fun shouldFetchFromApi(
        eventId: String,
        eventStatus: String,
        forceRefresh: Boolean
    ): Boolean {
        val isCompleted = eventStatus.equals("Completed", ignoreCase = true)
        val hasLocalData = dao.hasFightsForEvent(eventId)

        if (isCompleted && hasLocalData && !forceRefresh) {
            return false
        }

        if (!rateLimiter.shouldFetch(eventId)) {
            return false
        }
        return true
    }
}