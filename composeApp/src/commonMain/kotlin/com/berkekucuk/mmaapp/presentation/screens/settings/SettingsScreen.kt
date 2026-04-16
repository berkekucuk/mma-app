package com.berkekucuk.mmaapp.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppLanguage
import com.berkekucuk.mmaapp.core.presentation.LocalMeasurementUnit
import com.berkekucuk.mmaapp.core.presentation.LocalOddsFormat
import com.berkekucuk.mmaapp.core.presentation.MeasurementUnit
import com.berkekucuk.mmaapp.core.presentation.OddsFormat
import com.berkekucuk.mmaapp.core.presentation.ThemeMode
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onMeasurementUnitChange: (MeasurementUnit) -> Unit,
    onOddsFormatChange: (OddsFormat) -> Unit,
    currentThemeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current
    val currentLanguage = strings.language
    val currentMeasurementUnit = LocalMeasurementUnit.current
    val currentOddsFormat = LocalOddsFormat.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.pagerBackground,
        topBar = {
            Column(
                modifier = Modifier.background(colors.eventsTopBarGradient)
            ){
                TopAppBar(
                    title = {
                        Text(
                            text = strings.settingsTitle,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.contentDescriptionBack,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = colors.textPrimary,
                        titleContentColor = colors.textPrimary,
                    ),
                )
            }

        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            SettingsSection(title = strings.settingsSectionTheme) {
                SettingsOptionRow(
                    label = strings.settingsThemeLight,
                    isSelected = currentThemeMode == ThemeMode.LIGHT,
                    onClick = { onThemeModeChange(ThemeMode.LIGHT) },
                )
                HorizontalDivider(color = colors.dividerColor, thickness = 0.8.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsOptionRow(
                    label = strings.settingsThemeDark,
                    isSelected = currentThemeMode == ThemeMode.DARK,
                    onClick = { onThemeModeChange(ThemeMode.DARK) },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = strings.settingsSectionLanguage) {
                SettingsOptionRow(
                    label = "English",
                    isSelected = currentLanguage == AppLanguage.EN,
                    onClick = { onLanguageChange(AppLanguage.EN) },
                )
                HorizontalDivider(color = colors.dividerColor, thickness = 0.8.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsOptionRow(
                    label = "Türkçe",
                    isSelected = currentLanguage == AppLanguage.TR,
                    onClick = { onLanguageChange(AppLanguage.TR) },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = strings.settingsSectionMeasurements) {
                SettingsOptionRow(
                    label = "Metric (cm)",
                    isSelected = currentMeasurementUnit == MeasurementUnit.METRIC,
                    onClick = { onMeasurementUnitChange(MeasurementUnit.METRIC) },
                )
                HorizontalDivider(color = colors.dividerColor, thickness = 0.8.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsOptionRow(
                    label = "Imperial (ft/in)",
                    isSelected = currentMeasurementUnit == MeasurementUnit.IMPERIAL,
                    onClick = { onMeasurementUnitChange(MeasurementUnit.IMPERIAL) },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = strings.settingsSectionOdds) {
                SettingsOptionRow(
                    label = "Decimal",
                    isSelected = currentOddsFormat == OddsFormat.DECIMAL,
                    onClick = { onOddsFormatChange(OddsFormat.DECIMAL) },
                )
                HorizontalDivider(color = colors.dividerColor, thickness = 0.8.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsOptionRow(
                    label = "American",
                    isSelected = currentOddsFormat == OddsFormat.AMERICAN,
                    onClick = { onOddsFormatChange(OddsFormat.AMERICAN) },
                )
            }
        }
    }
}




