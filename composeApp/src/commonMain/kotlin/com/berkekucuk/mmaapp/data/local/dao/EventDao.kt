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

    @Query("SELECT * FROM events ORDER BY datetime_utc ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT MIN(datetime_utc) FROM events WHERE LOWER(status) != 'completed'")
    suspend fun getOldestPendingEventDate(): Long?

    @Query("SELECT EXISTS(SELECT 1 FROM events WHERE event_year = :year)")
    suspend fun hasEventsForYear(year: Int): Boolean
}
