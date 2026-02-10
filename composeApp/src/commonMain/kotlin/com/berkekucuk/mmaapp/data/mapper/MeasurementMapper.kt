package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.remote.dto.MeasurementDto
import com.berkekucuk.mmaapp.domain.model.Measurement
import com.berkekucuk.mmaapp.graphql.GetEventsQuery

fun GetEventsQuery.Height.toDto(): MeasurementDto {
    return MeasurementDto(
        metric = metric?.toIntOrNull(),
        imperial = imperial
    )
}

fun GetEventsQuery.Reach.toDto(): MeasurementDto {
    return MeasurementDto(
        metric = metric?.toIntOrNull(),
        imperial = imperial
    )
}
fun MeasurementDto.toDomain(): Measurement {
    return Measurement(
        metric = metric,
        imperial = imperial
    )
}