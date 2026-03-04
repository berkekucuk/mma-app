package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class FighterHistoryDto(
    @SerialName("event_name") val eventName: String? = null,
    @SerialName("event_date") val eventDate: Instant? = null,
    @SerialName("method_type") val methodType: String? = null,
    @SerialName("method_detail") val methodDetail: String? = null,
    @SerialName("round_summary") val roundSummary: String? = null,
    @SerialName("bout_type") val boutType: String? = null,
    @SerialName("weight_class_lbs") val weightClassLbs: Int? = null,
    @SerialName("rounds_format") val roundsFormat: String? = null,
    @SerialName("weight_class_id") val weightClassId: String? = null,
    @SerialName("odds_value") val oddsValue: Int? = null,
    @SerialName("odds_label") val oddsLabel: String? = null,
    @SerialName("result") val result: String? = null,
    @SerialName("record_after_fight") val recordAfterFight: String? = null,
    @SerialName("is_red_corner") val isRedCorner: Boolean? = null,

    @SerialName("opp_fighter_id") val oppFighterId: String? = null,
    @SerialName("opp_name") val oppName: String? = null,
    @SerialName("opp_image_url") val oppImageUrl: String? = null,
    @SerialName("opp_record") val oppRecord: RecordDto? = null,
    @SerialName("opp_fighting_out_of") val oppFightingOutOf: String? = null,
    @SerialName("opp_height") val oppHeight: MeasurementDto? = null,
    @SerialName("opp_reach") val oppReach: MeasurementDto? = null,
    @SerialName("opp_date_of_birth") val oppDateOfBirth: String? = null,
    @SerialName("opp_country_code") val oppCountryCode: String? = null,
    @SerialName("opp_odds_value") val oppOddsValue: Int? = null,
    @SerialName("opp_odds_label") val oppOddsLabel: String? = null,
    @SerialName("opp_result") val oppResult: String? = null,
    @SerialName("opp_record_after_fight") val oppRecordAfterFight: String? = null,
    @SerialName("opp_is_red_corner") val oppIsRedCorner: Boolean? = null,
) {
}
