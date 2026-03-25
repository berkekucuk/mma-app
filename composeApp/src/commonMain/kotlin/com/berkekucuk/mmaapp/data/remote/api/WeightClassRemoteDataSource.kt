package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.WeightClassDto

interface WeightClassRemoteDataSource {
    suspend fun fetchWeightClasses(): List<WeightClassDto>
}