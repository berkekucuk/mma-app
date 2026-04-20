package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.local.dao.UserDao
import com.berkekucuk.mmaapp.data.local.entity.FightNotificationEntity
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.data.mapper.toFavoriteEntities
import com.berkekucuk.mmaapp.data.mapper.toFavoriteEntity
import com.berkekucuk.mmaapp.data.remote.api.UserRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.User
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
    private val dao: UserDao
) : UserRepository {

    override fun getUser(userId: String): Flow<User?> {
        return dao.getUserWithFavorites(userId)
            .map { entity -> entity?.toDomain() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncUser(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val userDto = remoteDataSource.fetchUser(userId)
                dao.insertUser(userDto.toEntity())
                dao.insertUserFavorites(userDto.toFavoriteEntities())
                syncFightNotifications(userId)
            }.onFailure {
                if (it is CancellationException) throw it
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

    private suspend fun syncFightNotifications(userId: String) {
        val notifications = remoteDataSource.fetchFightNotifications(userId)
        dao.insertFightNotifications(notifications.map { it.toEntity() })
    }

    override fun getFightNotificationStatus(fightId: String, userId: String): Flow<Boolean> {
        return dao.getFightNotificationStatus(fightId, userId)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun addFightNotification(fightId: String, userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                remoteDataSource.upsertFightNotification(fightId, userId)
                dao.insertFightNotification(FightNotificationEntity(fightId = fightId, userId = userId))
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }

    override suspend fun removeFightNotification(fightId: String, userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                remoteDataSource.deleteFightNotification(fightId, userId)
                dao.deleteFightNotification(fightId, userId)
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }

    override suspend fun addFavoriteFighter(userId: String, fighter: Fighter): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                remoteDataSource.addFavoriteFighter(userId, fighter.fighterId)
                dao.insertUserFavorite(fighter.toFavoriteEntity(userId))
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }

    override suspend fun removeFavoriteFighter(userId: String, fighterId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                remoteDataSource.removeFavoriteFighter(userId, fighterId)
                dao.deleteUserFavorite(userId, fighterId)
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }
}
