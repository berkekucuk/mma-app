package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.remote.dto.RecordDto
import com.berkekucuk.mmaapp.domain.model.Record

fun RecordDto.toDomain(): Record {
    return Record(
        wins = wins ?: 0,
        losses = losses ?: 0,
        draws = draws ?: 0
    )
}

fun Record.toDto(): RecordDto {
    return RecordDto(
        wins = wins,
        losses = losses,
        draws = draws
    )
}