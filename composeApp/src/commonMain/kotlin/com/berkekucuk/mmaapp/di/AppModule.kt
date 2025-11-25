package com.berkekucuk.mmaapp.di

import org.koin.dsl.module
import com.berkekucuk.mmaapp.BuildConfig
import com.berkekucuk.mmaapp.data.remote.EventAPI
import com.berkekucuk.mmaapp.data.remote.SupabaseClientFactory
import com.berkekucuk.mmaapp.data.remote.EventRemoteDataSource
import com.berkekucuk.mmaapp.domain.repository.EventRepository
import com.berkekucuk.mmaapp.data.repository.EventRepositoryImpl
import com.berkekucuk.mmaapp.presentation.EventViewModel
import org.koin.core.module.dsl.viewModel

val appModule = module {

    single {
        SupabaseClientFactory.create(
            url = BuildConfig.SUPABASE_URL,
            key = BuildConfig.SUPABASE_KEY
        )
    }

    // 2. Remote Data Source
    single<EventRemoteDataSource> {
        EventAPI(supabase = get())
    }

    // 3. Repository
    single<EventRepository> {
        EventRepositoryImpl(remoteDataSource = get())
    }

    viewModel {
        EventViewModel(repository = get())
    }
}