package com.berkekucuk.mmaapp.data.remote.factory

import com.apollographql.apollo.ApolloClient

object ApolloClientFactory {
    fun create(url: String, apiKey: String): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(url)
            .addHttpHeader("x-api-key", apiKey)
            .build()
    }
}
