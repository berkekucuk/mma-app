package com.berkekucuk.mmaapp.di

import org.koin.dsl.module
import com.berkekucuk.mmaapp.BuildConfig
import com.berkekucuk.mmaapp.data.local.AppDatabase
import com.berkekucuk.mmaapp.data.local.getRoomDatabase
import com.berkekucuk.mmaapp.data.remote.api.EventAPI
import com.berkekucuk.mmaapp.data.remote.SupabaseClientFactory
import com.berkekucuk.mmaapp.data.remote.api.EventRemoteDataSource
import com.berkekucuk.mmaapp.data.repository.EventRepositoryImpl
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel

val appModule = module {

    includes(platformModule)

    // supabase client
    single {
        SupabaseClientFactory.create(
            url = BuildConfig.SUPABASE_URL,
            key = BuildConfig.SUPABASE_KEY
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
        EventAPI(supabase = get())
    }

    // repository
    single<EventRepository> {
        EventRepositoryImpl(
            remoteDataSource = get(),
            dao = get()
        )
    }

    // view model
    viewModel {
        HomeViewModel(eventRepository = get())
    }
}