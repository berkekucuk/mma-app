package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.local.dao.FightDao
import com.berkekucuk.mmaapp.data.local.dao.PredictionDao
import com.berkekucuk.mmaapp.data.remote.api.PredictionRemoteDataSource
import com.berkekucuk.mmaapp.domain.repository.PredictionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.domain.model.Prediction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PredictionRepositoryImpl(
    private val predictionDao: PredictionDao,
    private val fightDao: FightDao,
    private val remoteDataSource: PredictionRemoteDataSource
) : PredictionRepository {

    override fun getPredictedWinnerId(fightId: String, userId: String): Flow<String?> {
        return predictionDao.getPrediction(fightId, userId)
            .map { it?.prediction?.predictedWinnerId }
    }

    override fun getPredictions(userId: String): Flow<List<Prediction>> {
        return predictionDao.getPredictions(userId)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun syncPredictions(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val predictions = remoteDataSource.fetchPredictions(userId)
                
                val fights = predictions.mapNotNull { it.fight }
                if (fights.isNotEmpty()) {
                    fightDao.insertFights(fights.map { it.toEntity() })
                }
                
                predictionDao.replacePredictions(userId, predictions.map { it.toEntity() })
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }

    override suspend fun submitPrediction(
        userId: String,
        fightId: String,
        predictedWinnerId: String,
        lockedOdds: Int
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val predictionDto = remoteDataSource.submitPrediction(userId, fightId, predictedWinnerId, lockedOdds)
                predictionDao.insertPrediction(predictionDto.toEntity())
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }
}