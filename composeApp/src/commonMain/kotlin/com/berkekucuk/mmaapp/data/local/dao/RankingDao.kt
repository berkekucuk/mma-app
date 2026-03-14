package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.RankingEntity
import com.berkekucuk.mmaapp.data.local.entity.RankingSnapshotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RankingDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRankings(rankings: List<RankingEntity>)

    @Query("SELECT * FROM rankings")
    fun getAllRankings(): Flow<List<RankingEntity>>

    // Snapshot operations

    @Query("SELECT * FROM ranking_snapshots WHERE weight_class_id = :weightClassId")
    suspend fun getSnapshotsByWeightClass(weightClassId: String): List<RankingSnapshotEntity>

    @Query("SELECT * FROM ranking_snapshots")
    suspend fun getAllSnapshots(): List<RankingSnapshotEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshots(snapshots: List<RankingSnapshotEntity>)

    @Query("DELETE FROM ranking_snapshots")
    suspend fun deleteAllSnapshots()
}