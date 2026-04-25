package com.berkekucuk.mmaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.berkekucuk.mmaapp.core.app.App
import com.berkekucuk.mmaapp.core.app.Route
import com.berkekucuk.mmaapp.presentation.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private var initialRoute by mutableStateOf<Route?>(null)
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            viewModel.state.value.isLoading
        }
        handleIntent(intent)

        setContent {
            App(
                initialRoute = initialRoute,
                onRouteConsumed = { initialRoute = null }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val fightId = intent.getStringExtra("fight_id")
        if (fightId != null) {
            initialRoute = Route.FightDetail(fightId)
        }
    }
}
