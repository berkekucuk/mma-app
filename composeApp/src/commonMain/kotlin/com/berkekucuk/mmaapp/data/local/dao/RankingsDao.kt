package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.RankingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RankingsDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRankings(rankings: List<RankingEntity>)

    @Query("SELECT * FROM rankings")
    fun getAllRankings(): Flow<List<RankingEntity>>

    @Query("DELETE FROM rankings")
    suspend fun clearAllRankings()

    @Transaction
    suspend fun refreshRankings(rankings: List<RankingEntity>) {
        clearAllRankings()
        insertRankings(rankings)
    }
}