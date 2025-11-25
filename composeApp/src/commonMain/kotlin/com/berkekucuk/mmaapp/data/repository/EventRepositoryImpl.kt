package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.data.remote.EventRemoteDataSource
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.domain.model.Event

class EventRepositoryImpl(
    private val remoteDataSource: EventRemoteDataSource
) : EventRepository {

    override suspend fun getEvents(): Result<List<Event>> {
        return runCatching {

            val eventDtoList = remoteDataSource.fetchAllEvents()
            eventDtoList.map { it.toDomain() }
        }
    }
}