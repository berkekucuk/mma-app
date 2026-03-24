package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.data.local.dao.RankingDao
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.domain.model.Ranking
import com.berkekucuk.mmaapp.data.remote.api.RankingRemoteDataSource
import com.berkekucuk.mmaapp.domain.repository.RankingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class RankingRepositoryImpl(
    private val remoteDataSource: RankingRemoteDataSource,
    private val dao: RankingDao,
    private val rateLimiter: RateLimiter
) : RankingRepository {

    private companion object {
        private const val KEY_SYNC_RANKINGS = "sync_rankings"
    }

    override fun getRankings(weightClassId: String?): Flow<Map<String, List<Ranking>>> {
        return dao.getAllRankings()
            .map { entities ->
                entities
                    .let { if (weightClassId != null)
                        it.filter {
                        e -> e.weightClassId.equals(weightClassId, ignoreCase = true)
                        } else it
                    }
                    .map { it.toDomain() }
                    .groupBy { it.weightClassId }
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun hasRankings(): Boolean {
        return withContext(Dispatchers.IO) { dao.getCount() > 0 }
    }

    override suspend fun syncRankings(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(KEY_SYNC_RANKINGS)) {
                    return@runCatching
                }
                val rankingDtos = remoteDataSource.fetchRankings()
                val entities = rankingDtos.map { it.toEntity() }
                if (entities.isNotEmpty()) {
                    dao.insertRankings(entities)
                }
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(KEY_SYNC_RANKINGS)
            }
        }
    }
}