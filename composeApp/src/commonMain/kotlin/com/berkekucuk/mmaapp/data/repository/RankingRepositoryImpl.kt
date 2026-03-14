package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.data.local.dao.RankingDao
import com.berkekucuk.mmaapp.data.local.entity.RankingSnapshotEntity
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
import kotlin.time.Clock

class RankingRepositoryImpl(
    private val remoteDataSource: RankingRemoteDataSource,
    private val dao: RankingDao,
    private val rateLimiter: RateLimiter
) : RankingRepository {

    private companion object {
        private const val KEY_SYNC_RANKINGS = "sync_rankings"
    }

    override fun getRankings(): Flow<List<Ranking>> {
        return dao.getAllRankings()
            .map { entities ->
                val snapshots = dao.getAllSnapshots()
                val snapshotMap = snapshots.associateBy { it.fighterId to it.weightClassId }
                entities.map { entity -> 
                val fighterId = entity.fighter?.fighterId
                val rankChange = if (fighterId != null && snapshotMap.isNotEmpty()) {
                    val oldSnapshot = snapshotMap[fighterId to entity.weightClassId]
                    if (oldSnapshot != null) {
                        oldSnapshot.rankNumber - entity.rankNumber
                    } else {
                        null
                    }
                } else {
                    null
                }
                entity.toDomain(rankChange = rankChange)
                
                }
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncRankings(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(KEY_SYNC_RANKINGS)) {
                    return@runCatching
                }

                val currentSnapshots = dao.getAllSnapshots()
                val isFirstSync = currentSnapshots.isEmpty()

                val rankingDtos = remoteDataSource.fetchRankings()
                val entities = rankingDtos.map { it.toEntity() }

                if (entities.isNotEmpty()) {
                    val now = Clock.System.now().toEpochMilliseconds()
                    fun buildSnapshots(): List<RankingSnapshotEntity> {
                        return entities
                        .filter {
                            it.fighter?.fighterId != null
                        }
                        .groupBy {
                            it.fighter!!.fighterId to it.weightClassId
                        }
                        .map { (key, group) ->
                        val preferred = group.firstOrNull { it.rankNumber > 0} ?: group.first()
                        RankingSnapshotEntity(fighterId = key.first, 
                        weightClassId = key.second,
                        rankNumber = preferred.rankNumber,
                        snapshotDate = now
                        )
                    }}
                    dao.insertRankings(entities)

                    if(isFirstSync) {
                        dao.insertSnapshots(buildSnapshots())
                    }

                    if(!isFirstSync) {
                        dao.deleteAllSnapshots()
                        dao.insertSnapshots(buildSnapshots()) 
                    }
                }

                rateLimiter.markAsFetched(KEY_SYNC_RANKINGS)
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(KEY_SYNC_RANKINGS)
            }
        }
    }
}