package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.UserDto

interface UserRemoteDataSource {

    suspend fun fetchUser(userId: String): UserDto
    suspend fun fetchUsers(limit: Int): List<UserDto>

    suspend fun updateUser(userId: String, fullName: String, username: String)
    suspend fun deleteUser(userId: String)
}
