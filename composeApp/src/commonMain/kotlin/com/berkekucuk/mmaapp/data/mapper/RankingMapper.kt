package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.remote.dto.RankingDto
import com.berkekucuk.mmaapp.domain.model.RankedFighter
import com.berkekucuk.mmaapp.domain.model.Record
import com.berkekucuk.mmaapp.domain.model.WeightClassRanking

fun List<RankingDto>.toWeightClassRankings(): List<WeightClassRanking> {
    return this
        .groupBy { it.weightClassId }
        .map { (weightClassId, rankings) ->
            val firstRanking = rankings.first()
            val weightClass = firstRanking.weight_classes

            val fighters = rankings.map { dto ->
                RankedFighter(
                    fighterId = dto.fighterId,
                    name = dto.fighters?.name ?: "",
                    imageUrl = dto.fighters?.imageUrl ?: "",
                    rank = dto.rankNumber,
                    isChampion = dto.rankNumber == 0,
                    countryCode = dto.fighters?.countryCode ?: "",
                    record = dto.fighters?.record?.toDomain() ?: Record(0, 0, 0)
                )
            }.sortedBy { it.rank }

            WeightClassRanking(
                weightClassId = weightClassId,
                weightClassName = weightClass?.name ?: weightClassId,
                sortOrder = weightClass?.sortOrder ?: 0,
                champion = fighters.first { it.isChampion },
                rankedFighters = fighters.filter { !it.isChampion }
            )
        }
        .sortedBy { it.sortOrder }
}