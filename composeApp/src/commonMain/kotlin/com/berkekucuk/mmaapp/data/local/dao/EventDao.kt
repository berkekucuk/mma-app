package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

@Dao
interface EventDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM events WHERE event_id = :eventId")
    fun getEventById(eventId: String): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE LOWER(status) IN ('upcoming', 'live') ORDER BY datetime_utc ASC")
    fun getUpcomingEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE LOWER(status) = 'completed' AND event_year = :year ORDER BY datetime_utc DESC")
    fun getCompletedEventsByYear(year: Int): Flow<List<EventEntity>>

    @Query("SELECT MIN(datetime_utc) FROM events WHERE LOWER(status) NOT IN ('completed', 'cancelled')")
    suspend fun getOldestPendingEventDate(): Instant?

    @Query("SELECT EXISTS(SELECT 1 FROM events WHERE event_year = :year)")
    suspend fun hasEventsForYear(year: Int): Boolean
}
