package com.berkekucuk.mmaapp.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
lateinit var appContext: Context
actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = appContext.getDatabasePath("mma_app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}