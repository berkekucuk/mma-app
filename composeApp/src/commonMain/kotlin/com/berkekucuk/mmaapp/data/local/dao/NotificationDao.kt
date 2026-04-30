package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.berkekucuk.mmaapp.data.local.entity.FightNotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT COUNT(*) > 0 FROM fight_notifications WHERE fight_id = :fightId AND user_id = :userId")
    fun getFightNotificationStatus(fightId: String, userId: String): Flow<Boolean>

    @Upsert
    suspend fun upsertFightNotification(entity: FightNotificationEntity)

    @Upsert
    suspend fun upsertFightNotifications(entities: List<FightNotificationEntity>)

    @Query("DELETE FROM fight_notifications WHERE fight_id = :fightId AND user_id = :userId")
    suspend fun deleteFightNotification(fightId: String, userId: String)
}
