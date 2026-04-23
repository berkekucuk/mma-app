package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.UserDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class UserSupabaseAPI(
    private val client: SupabaseClient
) : UserRemoteDataSource {

    override suspend fun fetchUser(userId: String): UserDto {
        return client.from("profile_view").select {
            filter {
                eq("id", userId)
            }
        }.decodeSingle<UserDto>()
    }

    override suspend fun fetchUsers(limit: Int): List<UserDto> {
        return client.from("profile_view").select {
            limit(limit.toLong())
        }.decodeList<UserDto>()
    }

    override suspend fun updateUser(userId: String, fullName: String, username: String){
        client.from("profiles").update({
            set("username", username)
            set("full_name", fullName)
        }) {
            filter {
                eq("id", userId)
            }
        }
    }

    override suspend fun addFavoriteFighter(userId: String, fighterId: String) {
        client.from("user_favorite_fighters").insert(
            mapOf("user_id" to userId, "fighter_id" to fighterId)
        )
    }

    override suspend fun removeFavoriteFighter(userId: String, fighterId: String) {
        client.from("user_favorite_fighters").delete {
            filter {
                eq("user_id", userId)
                eq("fighter_id", fighterId)
            }
        }
    }
}
