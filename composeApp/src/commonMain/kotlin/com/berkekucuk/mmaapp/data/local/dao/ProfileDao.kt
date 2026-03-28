package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.berkekucuk.mmaapp.data.local.entity.FightNotificationEntity
import com.berkekucuk.mmaapp.data.local.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE id = :userId")
    fun getProfileById(userId: String): Flow<ProfileEntity?>

    @Query("SELECT COUNT(*) > 0 FROM fight_notifications WHERE fight_id = :fightId AND user_id = :userId")
    fun observeIsFightNotificationEnabled(fightId: String, userId: String): Flow<Boolean>

    @Query("UPDATE profiles SET username = :username, full_name = :fullName WHERE id = :userId")
    suspend fun updateProfile(userId: String, fullName: String, username: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFightNotification(entity: FightNotificationEntity)

    @Query("DELETE FROM fight_notifications WHERE fight_id = :fightId AND user_id = :userId")
    suspend fun deleteFightNotification(fightId: String, userId: String)
}