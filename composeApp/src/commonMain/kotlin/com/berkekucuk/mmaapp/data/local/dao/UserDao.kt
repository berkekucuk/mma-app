package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
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

    @androidx.room.Upsert
    suspend fun upsertUsers(users: List<UserEntity>)

    @Query("UPDATE users SET username = :username, full_name = :fullName WHERE id = :userId")
    suspend fun updateUser(userId: String, fullName: String, username: String)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    @Query("DELETE FROM users WHERE id NOT IN (:retainedIds) AND id != :currentUserId")
    suspend fun deleteUsersExcept(retainedIds: List<String>, currentUserId: String)

    @Transaction
    suspend fun replaceUsers(users: List<UserEntity>, currentUserId: String) {
        val newIds = users.map { it.id }
        upsertUsers(users)
        if (newIds.isNotEmpty()) {
            deleteUsersExcept(retainedIds = newIds, currentUserId = currentUserId)
        }
    }
}