package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.berkekucuk.mmaapp.data.local.entity.PredictionEntity
import com.berkekucuk.mmaapp.data.local.relation.PredictionWithFightRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictionDao {
    @Query("SELECT predicted_winner_id FROM predictions WHERE fight_id = :fightId AND user_id = :userId")
    fun getPredictedWinnerId(fightId: String, userId: String): Flow<String?>

    @Transaction
    @Query("SELECT * FROM predictions WHERE user_id = :userId ORDER BY created_at DESC")
    fun getPredictions(userId: String): Flow<List<PredictionWithFightRelation>>

    @Upsert
    suspend fun upsertPredictions(entities: List<PredictionEntity>)

    @Query("DELETE FROM predictions WHERE user_id = :userId")
    suspend fun deletePredictions(userId: String)

    @Query("DELETE FROM predictions WHERE user_id = :userId AND id NOT IN (:retainedIds)")
    suspend fun deletePredictionsExcept(userId: String, retainedIds: List<String>)

    @Transaction
    suspend fun replacePredictions(userId: String, entities: List<PredictionEntity>) {
        val newIds = entities.map { it.id }
        if (newIds.isEmpty()) {
            deletePredictions(userId)
        } else {
            deletePredictionsExcept(userId, newIds)
            upsertPredictions(entities)
        }
    }
}