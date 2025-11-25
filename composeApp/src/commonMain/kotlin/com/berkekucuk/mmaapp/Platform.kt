package com.berkekucuk.mmaapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform