package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FighterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFighter(fighter: FighterEntity)

    @Query("SELECT * FROM fighters WHERE fighter_id = :fighterId")
    fun getFighterById(fighterId: String): Flow<FighterEntity>
}
