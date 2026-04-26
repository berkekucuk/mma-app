package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.InteractionEntity
import com.berkekucuk.mmaapp.data.local.relation.InteractionWithFighterRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface InteractionDao {

    @Transaction
    @Query("SELECT * FROM user_fighter_interactions WHERE user_id = :userId AND (:type IS NULL OR interaction_type = :type) ORDER BY rank_number ASC")
    fun getInteractions(userId: String, type: String?): Flow<List<InteractionWithFighterRelation>>

    @Query("SELECT * FROM user_fighter_interactions WHERE id = :id")
    suspend fun getInteraction(id: String): InteractionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInteractions(interactions: List<InteractionEntity>)

    @Query("DELETE FROM user_fighter_interactions WHERE id = :interactionId")
    suspend fun deleteInteraction(interactionId: String)

    @Query("DELETE FROM user_fighter_interactions WHERE user_id = :userId")
    suspend fun deleteInteractions(userId: String)

    @Query("UPDATE user_fighter_interactions SET rank_number = rank_number - 1 WHERE user_id = :userId AND interaction_type = :type AND rank_number > :removedRank")
    suspend fun decrementRanksAbove(userId: String, type: String, removedRank: Int)

    @Transaction
    suspend fun replaceInteractions(userId: String, entities: List<InteractionEntity>) {
        deleteInteractions(userId)
        insertInteractions(entities)
    }
}
