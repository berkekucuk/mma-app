package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.remote.dto.WeightClassDto
import com.berkekucuk.mmaapp.domain.model.WeightClass

fun WeightClassDto.toDomain(): WeightClass{
    return WeightClass(
        name = name ?: "Unknown Weight Class",
        sortOrder = sortOrder ?: 0
    )
}