package com.berkekucuk.mmaapp.di

import org.koin.core.context.startKoin

fun doInitKoin() {
    startKoin {
        modules(appModule)
    }
}