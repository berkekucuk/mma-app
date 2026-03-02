package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.RankingEntity
import com.berkekucuk.mmaapp.data.remote.dto.RankingDto
import com.berkekucuk.mmaapp.domain.model.Ranking

fun RankingDto.toEntity(): RankingEntity {
    return RankingEntity(
        weightClassId = this.weightClassId,
        rankNumber = this.rankNumber ?: 0,
        fighter = this.fighter,
        weightClass = this.weightClass
    )
}

fun RankingEntity.toDomain(): Ranking {
    return Ranking(
        weightClassId = this.weightClassId,
        rankNumber = this.rankNumber,
        fighter = this.fighter?.toDomain(),
        weightClass = this.weightClass?.toDomain(),
    )
}