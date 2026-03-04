package com.berkekucuk.mmaapp.domain.enums

enum class WeightClass{
    STRAWWEIGHT,
    FLYWEIGHT,
    WOMENS_FLYWEIGHT,
    BANTAMWEIGHT,
    WOMENS_BANTAMWEIGHT,
    FEATHERWEIGHT,
    WOMENS_FEATHERWEIGHT,
    LIGHTWEIGHT,
    WELTERWEIGHT,
    MIDDLEWEIGHT,
    LIGHTHEAVYWEIGHT,
    HEAVYWEIGHT,
    CATCHWEIGHT,
    UNKNOWN;

    companion object {
        fun fromId(weightClassId: String?): WeightClass {
            return when (weightClassId?.lowercase()) {
                "sw" -> STRAWWEIGHT
                "flw" -> FLYWEIGHT
                "w_flw" -> WOMENS_FLYWEIGHT
                "bw" -> BANTAMWEIGHT
                "w_bw" -> WOMENS_BANTAMWEIGHT
                "fw" -> FEATHERWEIGHT
                "w_fw" -> WOMENS_FEATHERWEIGHT
                "lw" -> LIGHTWEIGHT
                "ww" -> WELTERWEIGHT
                "mw" -> MIDDLEWEIGHT
                "lhw" -> LIGHTHEAVYWEIGHT
                "hw" -> HEAVYWEIGHT
                "cw" -> CATCHWEIGHT
                else -> UNKNOWN
            }
        }
    }
}