package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.berkekucuk.mmaapp.data.local.entity.UserEntity
import com.berkekucuk.mmaapp.data.local.relation.UserProfileRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users ORDER BY total_points DESC, full_name ASC LIMIT :limit")
    fun getUsers(limit: Int): Flow<List<UserEntity>>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserProfile(userId: String): Flow<UserProfileRelation?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("UPDATE users SET username = :username, full_name = :fullName WHERE id = :userId")
    suspend fun updateUser(userId: String, fullName: String, username: String)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}