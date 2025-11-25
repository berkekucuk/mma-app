package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Event

interface EventRepository {

    suspend fun getEvents(): Result<List<Event>>
}