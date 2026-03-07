package com.berkekucuk.mmaapp.presentation.screens.fighter_detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.utils.calculateAgeAtDate
import com.berkekucuk.mmaapp.core.utils.formatDateOfBirth
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import kotlin.time.Clock

@Composable
fun FighterDetailContainer(
    fighter: Fighter,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    val age = remember(fighter.dateOfBirth) {
        calculateAgeAtDate(fighter.dateOfBirth, Clock.System.now())
    }

    val formattedDob = remember(fighter.dateOfBirth) {
        formatDateOfBirth(fighter.dateOfBirth)
    }

    val weightClassDisplay = remember(fighter.weightClassId) {
        fighter.weightClassId.name
            .replace("_", " ")
            .lowercase()
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
    }

    ListContainer(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        contentPadding = PaddingValues(top = 10.dp),
        verticalSpacing = 10.dp,
    ) {
        item(key = "record_card") {
            FighterRecordCard(record = fighter.record)
        }

        item(key = "info_card") {
            FighterInfoCard(
                fighter = fighter,
                age = age,
                formattedDob = formattedDob,
                weightClassDisplay = weightClassDisplay,
            )
        }
    }
}
