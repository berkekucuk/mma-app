package com.berkekucuk.mmaapp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.dsl.module
import com.berkekucuk.mmaapp.BuildConfig
import com.berkekucuk.mmaapp.data.local.AppDatabase
import com.berkekucuk.mmaapp.data.local.getDatabaseBuilder
import com.berkekucuk.mmaapp.data.remote.EventAPI
import com.berkekucuk.mmaapp.data.remote.SupabaseClientFactory
import com.berkekucuk.mmaapp.data.remote.EventRemoteDataSource
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.data.repository.EventRepositoryImpl
import com.berkekucuk.mmaapp.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel

val appModule = module {

    single {
        SupabaseClientFactory.create(
            url = BuildConfig.SUPABASE_URL,
            key = BuildConfig.SUPABASE_KEY
        )
    }

    single<AppDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single { get<AppDatabase>().eventDao() }

    // 2. Remote Data Source
    single<EventRemoteDataSource> {
        EventAPI(supabase = get())
    }

    // 3. Repository
    single<EventRepository> {
        EventRepositoryImpl(
            remoteDataSource = get(),
            dao = get())
    }

    viewModel {
        HomeViewModel(repository = get())
    }
}