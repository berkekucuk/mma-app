package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FightDao {
    @Query("SELECT * FROM fights WHERE fight_id = :fightId")
    fun getFight(fightId: String): Flow<FightEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFight(fight: FightEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFights(fights: List<FightEntity>)
}
