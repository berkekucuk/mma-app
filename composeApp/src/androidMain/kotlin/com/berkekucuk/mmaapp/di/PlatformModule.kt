package com.berkekucuk.mmaapp.di

import androidx.room.RoomDatabase
import com.berkekucuk.mmaapp.core.storage.AndroidLanguageStorage
import com.berkekucuk.mmaapp.core.storage.AndroidMeasurementUnitStorage
import com.berkekucuk.mmaapp.core.storage.AndroidOddsFormatStorage
import com.berkekucuk.mmaapp.core.storage.AndroidThemeStorage
import com.berkekucuk.mmaapp.core.storage.LanguageStorage
import com.berkekucuk.mmaapp.core.storage.MeasurementUnitStorage
import com.berkekucuk.mmaapp.core.storage.OddsFormatStorage
import com.berkekucuk.mmaapp.core.storage.ThemeStorage
import com.berkekucuk.mmaapp.data.local.AppDatabase
import com.berkekucuk.mmaapp.data.local.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder(androidContext())
    }
    single<LanguageStorage> {
        AndroidLanguageStorage(androidContext())
    }
    single<MeasurementUnitStorage> {
        AndroidMeasurementUnitStorage(androidContext())
    }
    single<OddsFormatStorage> {
        AndroidOddsFormatStorage(androidContext())
    }
    single<ThemeStorage> {
        AndroidThemeStorage(androidContext())
    }
}