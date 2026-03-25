package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.WeightClass
import kotlinx.coroutines.flow.Flow

interface WeightClassRepository {
    fun getAllWeightClasses(): Flow<List<WeightClass>>
    fun getWeightClassById(weightClassId: String): Flow<WeightClass?>
    suspend fun syncWeightClasses(): Result<Unit>
    suspend fun hasData(): Boolean
}
