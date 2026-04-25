package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.data.local.dao.UserDao
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.data.remote.api.UserRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.User
import com.berkekucuk.mmaapp.domain.model.UserProfile
import com.berkekucuk.mmaapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val dao: UserDao,
    private val rateLimiter: RateLimiter
) : UserRepository {

    private fun syncUserKey(userId: String) = "sync_user_$userId"
    private val syncUsersKey = "sync_users"

    override fun getUser(userId: String): Flow<User?> {
        return dao.getUser(userId)
            .map { entity -> entity?.toDomain() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override fun getUsers(limit: Int): Flow<List<User>> {
        return dao.getUsers(limit)
            .map { entities -> entities.map { it.toDomain() } }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override fun getUserProfile(userId: String): Flow<UserProfile?> {
        return dao.getUserProfile(userId)
            .map { it?.toDomain() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncUser(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(syncUserKey(userId))) {
                    return@runCatching
                }
                val userDto = remoteDataSource.fetchUser(userId)
                dao.insertUser(userDto.toEntity())
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(syncUserKey(userId))
            }
        }
    }

    override suspend fun syncUsers(limit: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (!rateLimiter.shouldFetch(syncUsersKey)) {
                    return@runCatching
                }
                val users = remoteDataSource.fetchUsers(limit)
                dao.insertUsers(users.map { it.toEntity() })
            }.onFailure {
                if (it is CancellationException) throw it
                rateLimiter.reset(syncUsersKey)
            }
        }
    }

    override suspend fun updateUser(userId: String, fullName: String, username: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                remoteDataSource.updateUser(userId, fullName, username)
                dao.updateUser(userId, fullName, username)
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }
}