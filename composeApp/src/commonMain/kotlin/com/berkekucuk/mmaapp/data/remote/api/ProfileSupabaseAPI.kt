package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.ProfileDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class ProfileSupabaseAPI(
    private val client: SupabaseClient
) : ProfileRemoteDataSource {

    override suspend fun fetchProfile(userId: String): ProfileDto {
        return client.from("profiles").select {
            filter {
                eq("id", userId)
            }
        }.decodeSingle<ProfileDto>()
    }

    override suspend fun updateProfile(userId: String, fullName: String, username: String){
        client.from("profiles").update({
            set("username", username)
            set("full_name", fullName)
        }) {
            filter {
                eq("id", userId)
            }
        }
    }
}
