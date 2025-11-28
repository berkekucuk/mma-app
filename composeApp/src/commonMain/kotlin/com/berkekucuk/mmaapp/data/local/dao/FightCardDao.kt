package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.local.entity.ParticipantEntity
import com.berkekucuk.mmaapp.data.local.relation.FightCard
import kotlinx.coroutines.flow.Flow

@Dao
interface FightCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFights(fights: List<FightEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFighters(fighters: List<FighterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<ParticipantEntity>)

    @Transaction
    suspend fun insertFullFightCardData(
        fights: List<FightEntity>,
        fighters: List<FighterEntity>,
        participants: List<ParticipantEntity>
    ) {
        insertFighters(fighters)
        insertFights(fights)
        insertParticipants(participants)
    }

    @Query("SELECT COUNT(*) FROM fights WHERE eventId = :eventId")
    suspend fun getFightCountForEvent(eventId: String): Int

    @Transaction
    @Query("SELECT * FROM fights WHERE eventId = :eventId ORDER BY fightOrder DESC")
    fun getFightCardsByEvent(eventId: String): Flow<List<FightCard>>
}