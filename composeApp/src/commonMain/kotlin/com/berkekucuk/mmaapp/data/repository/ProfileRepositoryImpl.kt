package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.local.dao.ProfileDao
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.data.remote.api.ProfileRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.Profile
import com.berkekucuk.mmaapp.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val dao: ProfileDao
) : ProfileRepository {

    override fun getProfile(userId: String): Flow<Profile?> {
        return dao.getProfileById(userId)
            .map { entity -> entity?.toDomain() }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun syncProfile(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val profileDto = remoteDataSource.fetchProfile(userId)
                dao.insertProfile(profileDto.toEntity())
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }

    override suspend fun updateUsername(userId: String, username: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val updatedDto = remoteDataSource.updateUsername(userId, username)
                dao.insertProfile(updatedDto.toEntity())
            }.onFailure {
                if (it is CancellationException) throw it
            }
        }
    }
}
