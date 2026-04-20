package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.FightNotificationEntity
import com.berkekucuk.mmaapp.data.local.entity.UserEntity
import com.berkekucuk.mmaapp.data.local.entity.UserFighterFavoriteEntity
import com.berkekucuk.mmaapp.data.local.entity.UserWithFavoriteFighters
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: String): Flow<UserEntity?>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserWithFavorites(userId: String): Flow<UserWithFavoriteFighters?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFavorites(favorites: List<UserFighterFavoriteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFavorite(favorite: UserFighterFavoriteEntity)

    @Query("DELETE FROM user_fighter_favorites WHERE user_id = :userId AND fighter_id = :fighterId")
    suspend fun deleteUserFavorite(userId: String, fighterId: String)

    @Query("UPDATE users SET username = :username, full_name = :fullName WHERE id = :userId")
    suspend fun updateUser(userId: String, fullName: String, username: String)

    @Query("SELECT COUNT(*) > 0 FROM fight_notifications WHERE fight_id = :fightId AND user_id = :userId")
    fun observeIsFightNotificationEnabled(fightId: String, userId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFightNotification(entity: FightNotificationEntity)

    @Query("DELETE FROM fight_notifications WHERE fight_id = :fightId AND user_id = :userId")
    suspend fun deleteFightNotification(fightId: String, userId: String)
}