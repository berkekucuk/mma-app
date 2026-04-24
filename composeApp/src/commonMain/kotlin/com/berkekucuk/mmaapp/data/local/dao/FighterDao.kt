package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.local.entity.FighterFightCrossRef
import com.berkekucuk.mmaapp.data.local.relation.FighterWithFightsRelation
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction

@Dao
interface FighterDao {

    @Transaction
    @Query("SELECT * FROM fighters WHERE fighter_id = :fighterId")
    fun getFighter(fighterId: String): Flow<FighterWithFightsRelation?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFighter(fighter: FighterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFighterFightCrossRefs(refs: List<FighterFightCrossRef>)

    @Query("DELETE FROM fighter_fights WHERE fighter_id = :fighterId")
    suspend fun deleteFighterFightCrossRefs(fighterId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM fighters WHERE fighter_id = :fighterId)")
    suspend fun isFighterExists(fighterId: String): Boolean
}
