package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.User
import com.berkekucuk.mmaapp.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userId: String): Flow<User?>
    fun getUsers(limit: Int): Flow<List<User>>
    fun getUserProfile(userId: String): Flow<UserProfile?>
    suspend fun syncUser(userId: String): Result<Unit>
    suspend fun syncUsers(limit: Int): Result<Unit>
    suspend fun updateUser(userId: String, fullName: String, username: String): Result<Unit>
    suspend fun deleteUser(userId: String): Result<Unit>
}
