package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.UserDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest

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

    override suspend fun deleteUser(userId: String) {
        client.postgrest.rpc("delete_my_account")
    }

    override suspend fun reportUser(reporterId: String, reportedId: String, reason: String) {
        client.from("user_reports").insert(
            mapOf(
                "reporter_id" to reporterId,
                "reported_id" to reportedId,
                "reason" to reason
            )
        )
    }
}
