package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.RankingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RankingDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRankings(rankings: List<RankingEntity>)

    @Query("SELECT * FROM rankings")
    fun getAllRankings(): Flow<List<RankingEntity>>

    @Query("SELECT * FROM rankings WHERE weight_class_id = :weightClassId")
    fun getRankingsByWeightClass(weightClassId: String): Flow<List<RankingEntity>>
}