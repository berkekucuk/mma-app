package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FightNotificationDto
import com.berkekucuk.mmaapp.data.remote.dto.UserDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class UserSupabaseAPI(
    private val client: SupabaseClient
) : UserRemoteDataSource {

    override suspend fun fetchUser(userId: String): UserDto {
        return client.from("profile_view").select{
            filter {
                eq("id", userId)
            }
        }.decodeSingle<UserDto>()
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

    override suspend fun fetchFightNotifications(userId: String): List<FightNotificationDto> {
        return client.from("user_fight_notifications").select {
            filter {
                eq("user_id", userId)
            }
        }.decodeList<FightNotificationDto>()
    }

    override suspend fun upsertFightNotification(fightId: String, userId: String) {
        client.from("user_fight_notifications").upsert(
            FightNotificationDto(userId = userId, fightId = fightId)
        )
    }

    override suspend fun deleteFightNotification(fightId: String, userId: String) {
        client.from("user_fight_notifications").delete {
            filter {
                eq("fight_id", fightId)
                eq("user_id", userId)
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
