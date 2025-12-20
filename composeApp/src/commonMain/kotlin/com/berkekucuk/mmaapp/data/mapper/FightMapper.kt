package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.remote.dto.FightDto
import com.berkekucuk.mmaapp.data.remote.dto.ParticipantDto
import com.berkekucuk.mmaapp.domain.enums.WeightClass
import com.berkekucuk.mmaapp.domain.model.Fight

fun FightDto.toEntity(): FightEntity {
    return FightEntity(
        fightId = fightId,
        eventId = eventId,
        methodType = methodType,
        methodDetail = methodDetail,
        roundSummary = roundSummary,
        boutType = boutType,
        weightClassLbs = weightClassLbs,
        weightClassId = weightClassId,
        roundsFormat = roundsFormat,
        fightOrder = fightOrder,
        participants = participants
    )
}

fun FightEntity.toDomain(): Fight = mapToFight(
    fightId, eventId, methodType, methodDetail, roundSummary,
    boutType, weightClassLbs, weightClassId, roundsFormat, fightOrder, participants
)

fun FightDto.toDomain(): Fight = mapToFight(
    fightId, eventId, methodType, methodDetail, roundSummary,
    boutType, weightClassLbs, weightClassId, roundsFormat, fightOrder, participants
)

private fun mapToFight(
    fightId: String,
    eventId: String,
    methodType: String?,
    methodDetail: String?,
    roundSummary: String?,
    boutType: String?,
    weightClassLbs: Int?,
    weightClassId: String?,
    roundsFormat: String?,
    fightOrder: Int?,
    participants: List<ParticipantDto>?
): Fight {
    return Fight(
        fightId = fightId,
        eventId = eventId,
        methodType = methodType ?: "",
        methodDetail = methodDetail ?: "",
        roundSummary = roundSummary ?: "",
        boutType = boutType ?: "",
        weightClassLbs = weightClassLbs,
        weightClass = parseWeightClass(weightClassId),
        roundsFormat = roundsFormat ?: "",
        fightOrder = fightOrder ?: 0,
        participants = participants?.map { it.toDomain() } ?: emptyList()
    )
}

private fun parseWeightClass(weightClassId: String?): WeightClass {
    return when (weightClassId?.lowercase()) {
        "sw" -> WeightClass.STRAWWEIGHT
        "flw" -> WeightClass.FLYWEIGHT
        "bw" -> WeightClass.BANTAMWEIGHT
        "fw" -> WeightClass.FEATHERWEIGHT
        "lw" -> WeightClass.LIGHTWEIGHT
        "ww" -> WeightClass.WELTERWEIGHT
        "mw" -> WeightClass.MIDDLEWEIGHT
        "lhw" -> WeightClass.LIGHTHEAVYWEIGHT
        "hw" -> WeightClass.HEAVYWEIGHT
        "cw" -> WeightClass.CATCHWEIGHT
        else -> WeightClass.UNKNOWN
    }
}
