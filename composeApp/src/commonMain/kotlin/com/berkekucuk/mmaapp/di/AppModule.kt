package com.berkekucuk.mmaapp.di

import org.koin.dsl.module
import com.berkekucuk.mmaapp.BuildConfig
import com.berkekucuk.mmaapp.core.utils.DateTimeProvider
import com.berkekucuk.mmaapp.core.utils.RateLimiter
import com.berkekucuk.mmaapp.core.utils.SystemDateTimeProvider
import com.berkekucuk.mmaapp.data.local.AppDatabase
import com.berkekucuk.mmaapp.data.local.getRoomDatabase
import com.berkekucuk.mmaapp.data.remote.factory.SupabaseClientFactory
import com.berkekucuk.mmaapp.data.remote.api.EventRemoteDataSource
import com.berkekucuk.mmaapp.data.remote.api.EventSupabaseAPI
import com.berkekucuk.mmaapp.data.remote.api.RankingsRemoteDataSource
import com.berkekucuk.mmaapp.data.remote.api.RankingsSupabaseAPI
import com.berkekucuk.mmaapp.data.repository.EventRepositoryImpl
import com.berkekucuk.mmaapp.data.repository.RankingRepositoryImpl
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.domain.repository.RankingRepository
import com.berkekucuk.mmaapp.presentation.screens.event_detail.EventDetailViewModel
import com.berkekucuk.mmaapp.presentation.screens.fight_detail.FightDetailViewModel
import com.berkekucuk.mmaapp.presentation.screens.home.HomeViewModel
import com.berkekucuk.mmaapp.presentation.screens.rankings.RankingViewModel
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
    single {
        SupabaseClientFactory.create(
            url = BuildConfig.SUPABASE_URL,
            key = BuildConfig.SUPABASE_KEY
        )
    }

    // apollo client
//    single {
//        ApolloClientFactory.create(
//            url = BuildConfig.APPSYNC_API_URL,
//            apiKey = BuildConfig.APPSYNC_API_KEY
//        )
//    }

    // local db
    single<AppDatabase> {
        getRoomDatabase(get())
    }

    single {
        get<AppDatabase>().eventDao()
    }

    single {
        get<AppDatabase>().rankingsDao()
    }

    // remote data source
    single<EventRemoteDataSource> {
        EventSupabaseAPI(client = get())
    }

    single<RankingsRemoteDataSource> {
        RankingsSupabaseAPI(client = get())
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

    single<RankingRepository> {
        RankingRepositoryImpl(
            remoteDataSource = get(),
            dao = get(),
            rateLimiter = get()
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

    viewModel {
        RankingViewModel(repository = get())
    }
}
