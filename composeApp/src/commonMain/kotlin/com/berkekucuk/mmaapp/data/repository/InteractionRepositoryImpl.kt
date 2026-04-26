package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.data.local.dao.FighterDao
import com.berkekucuk.mmaapp.data.local.dao.InteractionDao
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.data.remote.api.InteractionRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Interaction
import com.berkekucuk.mmaapp.domain.repository.InteractionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.collections.map
import kotlin.coroutines.cancellation.CancellationException

class InteractionRepositoryImpl(
    private val remoteDataSource: InteractionRemoteDataSource,
    private val interactionDao: InteractionDao,
    private val fighterDao: FighterDao,
    private val rateLimiter: RateLimiter
) : InteractionRepository {

    private fun syncKey(userId: String) = "sync_interactions_$userId"

    override fun getInteractions(userId: String, type: String?): Flow<List<Interaction>> {
        return interactionDao.getInteractions(userId, type)
            .map { entities -> entities.map { it.toDomain() } }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncInteractions(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(syncKey(userId))) {
                    return@runCatching
                }
                val interactions = remoteDataSource.fetchInteractions(userId)

                val fighters = interactions.mapNotNull { it.fighter }
                if (fighters.isNotEmpty()) {
                    fighterDao.insertFighters(fighters.map { it.toEntity() })
                }

                interactionDao.replaceInteractions(userId, interactions.map { it.toEntity() })
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(syncKey(userId))
            }
        }
    }

    override suspend fun addInteraction(
        userId: String,
        fighterId: String,
        interactionType: String,
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val dto = remoteDataSource.addInteraction(userId, fighterId, interactionType)
                interactionDao.insertInteractions(listOf(dto.toEntity()))
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }

    override suspend fun removeInteraction(interactionId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                remoteDataSource.removeInteraction(interactionId)
                interactionDao.deleteInteraction(interactionId)
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }
}
