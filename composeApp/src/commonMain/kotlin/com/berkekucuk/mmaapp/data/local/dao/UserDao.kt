package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.berkekucuk.mmaapp.data.local.entity.FightNotificationEntity
import com.berkekucuk.mmaapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT :limit")
    fun getUsers(limit: Int): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("UPDATE users SET username = :username, full_name = :fullName WHERE id = :userId")
    suspend fun updateUser(userId: String, fullName: String, username: String)

    @Query("SELECT COUNT(*) > 0 FROM fight_notifications WHERE fight_id = :fightId AND user_id = :userId")
    fun getFightNotificationStatus(fightId: String, userId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFightNotification(entity: FightNotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFightNotifications(entities: List<FightNotificationEntity>)

    @Query("DELETE FROM fight_notifications WHERE fight_id = :fightId AND user_id = :userId")
    suspend fun deleteFightNotification(fightId: String, userId: String)
}