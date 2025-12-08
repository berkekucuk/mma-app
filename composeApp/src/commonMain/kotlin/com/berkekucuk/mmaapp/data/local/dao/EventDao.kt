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

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int

    @Query("SELECT COUNT(*) FROM events WHERE datetime_utc >= :start AND datetime_utc <= :end")
    suspend fun getEventCountForYear(start: Long, end: Long): Int

    @Query("SELECT MIN(datetime_utc) FROM events WHERE LOWER(status) != 'completed'")
    suspend fun getOldestPendingEventDate(): Long?
}