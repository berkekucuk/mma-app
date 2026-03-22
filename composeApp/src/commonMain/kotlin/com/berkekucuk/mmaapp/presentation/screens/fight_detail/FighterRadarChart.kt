package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.Participant
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private data class RadarData(
    val values: List<Float>,
    val color: Color,
    val fillColor: Color,
)

@Composable
fun FighterRadarChart(
    redCorner: Participant?,
    blueCorner: Participant?,
    redFighterFull: Fighter?,
    blueFighterFull: Fighter?,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val axisLabels = listOf(
        strings.radarLabelHeight,
        strings.radarLabelReach,
        strings.radarLabelOdds,
        strings.radarLabelWinRate,
        strings.radarLabelKoTkoRate,
        strings.radarLabelSubRate,
    )
    val redRates = remember(redFighterFull) { redFighterFull?.calculateRates() ?: FighterRates(0f, 0f, 0f) }
    val blueRates = remember(blueFighterFull) { blueFighterFull?.calculateRates() ?: FighterRates(0f, 0f, 0f) }
    val redData = remember(redCorner, redRates) {
        RadarData(
            values = buildRadarValues(redCorner, redRates),
            color = Color(0xFFE53935),
            fillColor = Color(0x40E53935),
        )
    }
    val blueData = remember(blueCorner, blueRates) {
        RadarData(
            values = buildRadarValues(blueCorner, blueRates),
            color = Color(0xFF1E88E5),
            fillColor = Color(0x401E88E5),
        )
    }
    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.fightItemBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                val radius = size.minDimension * 0.34f

                for (level in 1..3) {
                    val r = radius * level / 3f
                    val gridPath = Path()
                    for (i in 0 until RADAR_AXIS_COUNT) {
                        val angle = (2 * PI / RADAR_AXIS_COUNT) * i - PI / 2
                        val x = centerX + r * cos(angle).toFloat()
                        val y = centerY + r * sin(angle).toFloat()
                        if (i == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
                    }
                    gridPath.close()
                    drawPath(gridPath, Color(0xFF3A3A3A), style = Stroke(width = 1f))
                }

                for (i in 0 until RADAR_AXIS_COUNT) {
                    val angle = (2 * PI / RADAR_AXIS_COUNT) * i - PI / 2
                    val x = centerX + radius * cos(angle).toFloat()
                    val y = centerY + radius * sin(angle).toFloat()
                    drawLine(Color(0xFF3A3A3A), Offset(centerX, centerY), Offset(x, y), strokeWidth = 1f)
                }

                drawRadarPolygon(this, centerX, centerY, radius, redData)
                drawRadarPolygon(this, centerX, centerY, radius, blueData)
                drawRadarLabels(this, textMeasurer, centerX, centerY, radius, axisLabels)
            }
        }
        RadarCornerRow(
            redName = redCorner?.fighter?.name ?: "Red",
            blueName = blueCorner?.fighter?.name ?: "Blue",
        )
    }
}

@Composable
private fun RadarCornerRow(redName: String, blueName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadarCornerItem(color = Color(0xFFE53935), name = redName)
        RadarCornerItem(color = Color(0xFF1E88E5), name = blueName)
    }
}

@Composable
private fun RadarCornerItem(color: Color, name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Canvas(modifier = Modifier.size(10.dp)) {
            drawCircle(color, radius = 5.dp.toPx())
        }
        Text(
            text = name,
            color = AppColors.textSecondary,
            fontSize = 11.sp,
        )
    }
}

private fun drawRadarPolygon(
    drawScope: DrawScope,
    centerX: Float,
    centerY: Float,
    radius: Float,
    data: RadarData,
) {
    val path = Path()
    data.values.forEachIndexed { i, value ->
        val angle = (2 * PI / RADAR_AXIS_COUNT) * i - PI / 2
        val r = radius * value
        val x = centerX + r * cos(angle).toFloat()
        val y = centerY + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawScope.drawPath(path, data.fillColor, style = Fill)
    drawScope.drawPath(path, data.color, style = Stroke(width = 2f))
}

private fun drawRadarLabels(
    drawScope: DrawScope,
    textMeasurer: TextMeasurer,
    centerX: Float,
    centerY: Float,
    radius: Float,
    labels: List<String>,
) {
    val labelRadius = radius * 1.18f
    val style = TextStyle(
        color = Color(0xFF8B8E90),
        fontSize = 10.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
        textAlign = TextAlign.Center,
    )
    labels.forEachIndexed { i, label ->
        val angle = (2 * PI / RADAR_AXIS_COUNT) * i - PI / 2
        val x = centerX + labelRadius * cos(angle).toFloat()
        val y = centerY + labelRadius * sin(angle).toFloat()
        val textLayout = textMeasurer.measure(label, style)
        drawScope.drawText(
            textLayoutResult = textLayout,
            topLeft = Offset(
                x = x - textLayout.size.width / 2f,
                y = y - textLayout.size.height / 2f,
            ),
        )
    }
}