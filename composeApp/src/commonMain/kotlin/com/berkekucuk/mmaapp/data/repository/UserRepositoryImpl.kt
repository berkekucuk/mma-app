package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.local.dao.UserDao
import com.berkekucuk.mmaapp.data.local.entity.FightNotificationEntity
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.data.mapper.toFavoriteDto
import com.berkekucuk.mmaapp.data.remote.api.UserRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.User
import com.berkekucuk.mmaapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val dao: UserDao
) : UserRepository {

    override fun getUser(userId: String): Flow<User?> {
        return dao.getUser(userId)
            .map { entity -> entity?.toDomain() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncUser(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val userDto = remoteDataSource.fetchUser(userId)
                dao.insertUser(userDto.toEntity())
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
                val user = dao.getUser(userId).first() ?: return@runCatching
                remoteDataSource.addFavoriteFighter(userId, fighter.fighterId)
                val newList = (user.favoriteFighters + fighter.toFavoriteDto())
                    .mapIndexed { index, item -> item.copy(rankNumber = index + 1) }
                dao.insertUser(user.copy(favoriteFighters = newList))
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }

    override suspend fun removeFavoriteFighter(userId: String, fighterId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val user = dao.getUser(userId).first() ?: return@runCatching
                remoteDataSource.removeFavoriteFighter(userId, fighterId)
                val updated = user.favoriteFighters
                    .filter { it.fighter?.fighterId != fighterId }
                    .mapIndexed { index, item -> item.copy(rankNumber = index + 1) }
                dao.insertUser(user.copy(favoriteFighters = updated))
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }
}