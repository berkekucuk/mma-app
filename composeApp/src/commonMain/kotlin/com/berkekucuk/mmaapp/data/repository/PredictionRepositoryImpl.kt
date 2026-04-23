package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.local.dao.PredictionDao
import com.berkekucuk.mmaapp.data.local.entity.PredictionEntity
import com.berkekucuk.mmaapp.data.remote.api.PredictionRemoteDataSource
import com.berkekucuk.mmaapp.domain.repository.PredictionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PredictionRepositoryImpl(
    private val dao: PredictionDao,
    private val remoteDataSource: PredictionRemoteDataSource
) : PredictionRepository {

    override fun getPredictedWinnerId(fightId: String, userId: String): Flow<String?> {
        return dao.getPrediction(fightId, userId).map { it?.predictedWinnerId }
    }

    override suspend fun submitPrediction(userId: String, fightId: String, predictedWinnerId: String, lockedOdds: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                remoteDataSource.submitPrediction(userId, fightId, predictedWinnerId, lockedOdds)
                dao.insertPrediction(
                    PredictionEntity(
                        fightId = fightId,
                        userId = userId,
                        predictedWinnerId = predictedWinnerId,
                        lockedOdds = lockedOdds
                    )
                )
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }
}
