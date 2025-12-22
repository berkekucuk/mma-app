package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FightDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFights(fights: List<FightEntity>)

    @Query("SELECT * FROM fights WHERE event_id = :eventId ORDER BY fight_order ASC")
    fun getFightsByEvent(eventId: String): Flow<List<FightEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM fights WHERE event_id = :eventId)")
    suspend fun hasFightsForEvent(eventId: String): Boolean
}
