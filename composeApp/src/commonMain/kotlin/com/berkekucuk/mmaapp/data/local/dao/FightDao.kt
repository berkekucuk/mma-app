package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.local.entity.ParticipantEntity
import com.berkekucuk.mmaapp.data.local.relation.PopulatedFight
import kotlinx.coroutines.flow.Flow

@Dao
interface FightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFights(fights: List<FightEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFighters(fighters: List<FighterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<ParticipantEntity>)


    @Transaction
    suspend fun insertFullEventData(
        fights: List<FightEntity>,
        fighters: List<FighterEntity>,
        participants: List<ParticipantEntity>
    ) {
        insertFighters(fighters)
        insertFights(fights)
        insertParticipants(participants)
    }

    @Transaction
    @Query("SELECT * FROM fights WHERE eventId = :eventId ORDER BY fightOrder DESC")
    fun getFightsByEventId(eventId: String): Flow<List<PopulatedFight>>
}