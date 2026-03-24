package com.berkekucuk.mmaapp.domain.model

object WeightClassRegistry {

    private val sortOrders: Map<String, Int> = mapOf(
        "mens_p4p"   to 1,
        "flw"        to 2,
        "bw"         to 3,
        "fw"         to 4,
        "lw"         to 5,
        "ww"         to 6,
        "mw"         to 7,
        "lhw"        to 8,
        "hw"         to 9,
        "womens_p4p" to 10,
        "sw"         to 11,
        "w_flw"      to 12,
        "w_bw"       to 13,
        "w_fw"       to 14,
    )

    fun sortOrderOf(weightClassId: String): Int = sortOrders[weightClassId.lowercase()] ?: Int.MAX_VALUE
}
