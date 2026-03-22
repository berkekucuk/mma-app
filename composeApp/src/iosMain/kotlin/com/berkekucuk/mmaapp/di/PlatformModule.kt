package com.berkekucuk.mmaapp.di

import androidx.room.RoomDatabase
import com.berkekucuk.mmaapp.core.storage.IosLanguageStorage
import com.berkekucuk.mmaapp.core.storage.IosMeasurementUnitStorage
import com.berkekucuk.mmaapp.core.storage.IosOddsFormatStorage
import com.berkekucuk.mmaapp.core.storage.LanguageStorage
import com.berkekucuk.mmaapp.core.storage.MeasurementUnitStorage
import com.berkekucuk.mmaapp.core.storage.OddsFormatStorage
import com.berkekucuk.mmaapp.data.local.AppDatabase
import com.berkekucuk.mmaapp.data.local.getDatabaseBuilder
import org.koin.dsl.module

actual val platformModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
    single<LanguageStorage> {
        IosLanguageStorage()
    }
    single<MeasurementUnitStorage> {
        IosMeasurementUnitStorage()
    }
    single<OddsFormatStorage> {
        IosOddsFormatStorage()
    }
}