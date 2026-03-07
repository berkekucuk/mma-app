package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.data.local.dao.FighterDao
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.data.remote.api.FighterRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.repository.FighterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class FighterRepositoryImpl(
    private val remoteDataSource: FighterRemoteDataSource,
    private val dao: FighterDao,
    private val rateLimiter: RateLimiter
) : FighterRepository {

    private fun syncKey(fighterId: String) = "sync_fighter_$fighterId"

    override fun getFighterById(fighterId: String): Flow<Fighter> {
        return dao.getFighterById(fighterId)
            .filterNotNull()
            .map { entity -> entity.toDomain() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncFighter(fighterId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(syncKey(fighterId))) {
                    return@runCatching
                }

                val fighterDto = remoteDataSource.fetchFighterById(fighterId)
                dao.insertFighter(fighterDto.toEntity())

                rateLimiter.markAsFetched(syncKey(fighterId))
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(syncKey(fighterId))
            }
        }
    }
}
