package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.PredictionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictionDao {
    @Query("SELECT * FROM predictions WHERE fight_id = :fightId AND user_id = :userId")
    fun getPrediction(fightId: String, userId: String): Flow<PredictionEntity?>

    @Query("SELECT * FROM predictions WHERE user_id = :userId ORDER BY created_at DESC")
    fun getPredictions(userId: String): Flow<List<PredictionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrediction(entity: PredictionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPredictions(entities: List<PredictionEntity>)

    @Query("DELETE FROM predictions WHERE user_id = :userId")
    suspend fun deletePredictionsByUserId(userId: String)

    @Transaction
    suspend fun syncUserPredictions(userId: String, entities: List<PredictionEntity>) {
        deletePredictionsByUserId(userId)
        insertPredictions(entities)
    }
}
