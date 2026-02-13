package com.berkekucuk.mmaapp.data.remote.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.berkekucuk.mmaapp.data.mapper.toDto
import com.berkekucuk.mmaapp.data.remote.dto.EventDto
import com.berkekucuk.mmaapp.graphql.GetEventsQuery
import kotlin.time.Instant

class EventGraphqlAPI(
    private val apolloClient: ApolloClient
) : EventRemoteDataSource {

    override suspend fun fetchEventsById(id: String): List<EventDto> {
        val response = apolloClient.query(
            GetEventsQuery(eventId = Optional.Present(id))
        ).execute()
        return response.data?.getEvents?.mapNotNull { it?.toDto() } ?: emptyList()
    }

    override suspend fun fetchEventsByYear(year: Int): List<EventDto> {
        val response = apolloClient.query(
            GetEventsQuery(year = Optional.Present(year))
        ).execute()
        return response.data?.getEvents?.mapNotNull { it?.toDto() } ?: emptyList()
    }

    override suspend fun fetchEventsAfter(date: Instant): List<EventDto> {
        val dateString = date.toString().replace("Z", "+00:00")
        val response = apolloClient.query(
            GetEventsQuery(afterDate = Optional.Present(dateString))
        ).execute()
        return response.data?.getEvents?.mapNotNull { it?.toDto() } ?: emptyList()
    }
}