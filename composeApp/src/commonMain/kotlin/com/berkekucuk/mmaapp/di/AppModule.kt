package com.berkekucuk.mmaapp.di

import org.koin.dsl.module
import com.berkekucuk.mmaapp.BuildConfig
import com.berkekucuk.mmaapp.core.utils.DateTimeProvider
import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.core.utils.SystemDateTimeProvider
import com.berkekucuk.mmaapp.data.local.AppDatabase
import com.berkekucuk.mmaapp.data.local.getRoomDatabase
import com.berkekucuk.mmaapp.data.remote.factory.ApolloClientFactory
import com.berkekucuk.mmaapp.data.remote.api.EventGraphqlAPI
import com.berkekucuk.mmaapp.data.remote.api.EventRemoteDataSource
import com.berkekucuk.mmaapp.data.repository.EventRepositoryImpl
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.presentation.screens.event_detail.EventDetailViewModel
import com.berkekucuk.mmaapp.presentation.screens.fight_detail.FightDetailViewModel
import com.berkekucuk.mmaapp.presentation.screens.home.HomeViewModel
import org.koin.core.module.dsl.viewModel

val appModule = module {

    includes(platformModule)

    // time provider
    single<DateTimeProvider> { SystemDateTimeProvider() }

    // rate limiter
    single {
        RateLimiter(
            dateTimeProvider = get(),
            timeoutMs = 10_000L
        )
    }

    // supabase client
    //    single {
    //        SupabaseClientFactory.create(
    //            url = BuildConfig.SUPABASE_URL,
    //            key = BuildConfig.SUPABASE_KEY
    //        )
    //    }

    // apollo client
    single {
        ApolloClientFactory.create(
            url = BuildConfig.APPSYNC_API_URL,
            apiKey = BuildConfig.APPSYNC_API_KEY
        )
    }

    // local db
    single<AppDatabase> {
        getRoomDatabase(get())
    }

    single {
        get<AppDatabase>().eventDao()
    }

    // remote data source
    single<EventRemoteDataSource> {
        EventGraphqlAPI(apolloClient = get())
    }

    // repository
    single<EventRepository> {
        EventRepositoryImpl(
            remoteDataSource = get(),
            dao = get(),
            rateLimiter = get(),
            dateTimeProvider = get()
        )
    }

    // view model
    viewModel {
        HomeViewModel(
            eventRepository = get(),
            dateTimeProvider = get()
        )
    }

    viewModel {
        EventDetailViewModel(
            eventRepository = get(),
            savedStateHandle = get()
        )
    }

    viewModel {
        FightDetailViewModel(
            eventRepository = get(),
            savedStateHandle = get()
        )
    }
}
