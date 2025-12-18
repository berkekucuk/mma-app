package com.berkekucuk.mmaapp.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelRoute<T : Route>(
    val name: String,
    val route: T,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    TopLevelRoute("Home", Route.Home, Icons.Filled.Home),
    TopLevelRoute("Rankings", Route.Rankings, Icons.Filled.Star),
    TopLevelRoute("Profile", Route.Profile, Icons.Filled.Person)
)