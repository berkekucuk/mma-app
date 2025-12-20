package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Record(
    val wins: Int,
    val losses: Int,
    val draws: Int
) {
    override fun toString(): String = "$wins-$losses-$draws"

    companion object {
        val EMPTY = Record(wins = 0, losses = 0, draws = 0)
    }
}

