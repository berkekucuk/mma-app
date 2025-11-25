package com.berkekucuk.mmaapp.data.remote

import com.berkekucuk.mmaapp.data.dto.EventDto

interface EventRemoteDataSource {
    suspend fun fetchAllEvents(): List<EventDto>

}