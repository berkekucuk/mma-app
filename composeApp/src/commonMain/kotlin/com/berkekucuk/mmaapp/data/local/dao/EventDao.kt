package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM events ORDER BY datetimeUtc ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int

    @Query("""
        SELECT datetimeUtc FROM events 
        WHERE status NOT IN ('Completed') 
        ORDER BY datetimeUtc ASC 
        LIMIT 1
    """)
    suspend fun getOldestPendingEventDate(): Long?

}