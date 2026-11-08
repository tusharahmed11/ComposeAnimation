package com.imtusharahmed.composeanimation.ui.screens.infinite

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtusharahmed.composeanimation.ui.common.LocalAnimationSpeed
import com.imtusharahmed.composeanimation.ui.common.MotionCard
import com.imtusharahmed.composeanimation.ui.common.MotionStage
import com.imtusharahmed.composeanimation.ui.common.TelemetryStrip
import com.imtusharahmed.composeanimation.ui.common.scaledDuration
import com.imtusharahmed.composeanimation.ui.theme.CyberPink
import com.imtusharahmed.composeanimation.ui.theme.CyberViolet
import com.imtusharahmed.composeanimation.ui.theme.ElectricCyan
import com.imtusharahmed.composeanimation.ui.theme.NeonGreen
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBase
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorder
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorderSubtle
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InfiniteAnimationsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        RadarBeaconCard()
        ShimmerBrushCard()
        GlowingBreathingBorderCard()
    }
}

/* =======================================================================
   1. Radar Beacon: Expanding Multi-Ring Pulse + Sweep Line
   ======================================================================= */
@Composable
private fun RadarBeaconCard() {
    val speed = LocalAnimationSpeed.current
    val infiniteTransition = rememberInfiniteTransition(label = "RadarBeaconTransition")
    val pulseDuration = scaledDuration(2400, speed)

    val ring1Progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = pulseDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring1"
    )
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = scaledDuration(3200, speed), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarSweep"
    )

    val ring2Progress = (ring1Progress + 0.33f) % 1f
    val ring3Progress = (ring1Progress + 0.66f) % 1f

    MotionCard(
        title = "Radar Beacon & Sweep",
        apiTag = "rememberInfiniteTransition",
        subtitle = "Harmonic 3-phase expanding radial waves with continuous polar scanner sweep",
        accentColor = ElectricCyan
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TelemetryStrip(
                items = listOf(
                    "Sweep" to "${sweepAngle.toInt()}°",
                    "Phase 1" to String.format(Locale.US, "%.2f", ring1Progress),
                    "Mode" to "Restart"
                ),
                accentColor = ElectricCyan
            )

            MotionStage(height = 185.dp) {
                Canvas(modifier = Modifier.size(160.dp)) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val maxRadius = size.minDimension / 2f

                    // Static polar grid circles & crosshairs
                    drawCircle(color = ObsidianBorder.copy(alpha = 0.55f), radius = maxRadius * 0.35f, style = Stroke(1.dp.toPx()))
                    drawCircle(color = ObsidianBorder.copy(alpha = 0.55f), radius = maxRadius * 0.7f, style = Stroke(1.dp.toPx()))
                    drawCircle(color = ObsidianBorder.copy(alpha = 0.65f), radius = maxRadius, style = Stroke(1.dp.toPx()))
                    drawLine(
                        color = ObsidianBorder.copy(alpha = 0.45f),
                        start = Offset(center.x - maxRadius, center.y),
                        end = Offset(center.x + maxRadius, center.y),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = ObsidianBorder.copy(alpha = 0.45f),
                        start = Offset(center.x, center.y - maxRadius),
                        end = Offset(center.x, center.y + maxRadius),
                        strokeWidth = 1.dp.toPx()
                    )

                    // 3 expanding pulse rings
                    val rings = listOf(ring1Progress, ring2Progress, ring3Progress)
                    rings.forEach { p ->
                        val radius = p * maxRadius
                        val alpha = (1f - p).coerceIn(0f, 1f)
                        drawCircle(
                            color = ElectricCyan.copy(alpha = alpha * 0.85f),
                            radius = radius,
                            center = center,
                            style = Stroke(width = 2.2.dp.toPx())
                        )
                    }

                    // Rotating scanner line
                    val rad = (sweepAngle * PI / 180.0).toFloat()
                    val endX = center.x + maxRadius * cos(rad)
                    val endY = center.y + maxRadius * sin(rad)
                    drawLine(
                        color = NeonGreen.copy(alpha = 0.85f),
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Target blips
                    val blip1 = Offset(center.x + maxRadius * 0.45f, center.y - maxRadius * 0.3f)
                    val blip2 = Offset(center.x - maxRadius * 0.35f, center.y + maxRadius * 0.45f)
                    drawCircle(color = NeonGreen.copy(alpha = 0.85f), radius = 3.5.dp.toPx(), center = blip1)
                    drawCircle(color = CyberPink.copy(alpha = 0.85f), radius = 3.5.dp.toPx(), center = blip2)

                    // Center beacon core
                    drawCircle(color = ElectricCyan.copy(alpha = 0.28f), radius = 14.dp.toPx(), center = center)
                    drawCircle(color = ElectricCyan, radius = 6.dp.toPx(), center = center)
                    drawCircle(color = Color.White, radius = 2.5.dp.toPx(), center = center)
                }
            }
        }
    }
}

/* =======================================================================
   2. Shimmer Brush: Skeleton Linear Gradient Translation Sweep
   ======================================================================= */
@Composable
private fun ShimmerBrushCard() {
    val speed = LocalAnimationSpeed.current
    val infiniteTransition = rememberInfiniteTransition(label = "ShimmerTransition")
    val shimmerDuration = scaledDuration(1350, speed)

    val translateOffset by infiniteTransition.animateFloat(
        initialValue = -350f,
        targetValue = 850f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = shimmerDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            ObsidianElevated,
            ObsidianElevated,
            ElectricCyan.copy(alpha = 0.35f),
            CyberViolet.copy(alpha = 0.35f),
            ObsidianElevated,
            ObsidianElevated
        ),
        start = Offset(translateOffset, 0f),
        end = Offset(translateOffset + 340f, 140f)
    )

    MotionCard(
        title = "Shimmer Sweep Engine",
        apiTag = "Brush.linearGradient + animateFloat",
        subtitle = "Continuous diagonal highlight sweep over skeleton telemetry cards",
        accentColor = CyberViolet
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ObsidianBase)
                .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(shimmerBrush)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.82f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(shimmerBrush)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.55f)
                            .height(11.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(shimmerBrush)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(56.dp)
                        .height(26.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(shimmerBrush)
                    )
                }
            }
        }
    }
}

/* =======================================================================
   3. Glowing Breathing Border: animateColor with RepeatMode.Reverse
   ======================================================================= */
@Composable
private fun GlowingBreathingBorderCard() {
    val speed = LocalAnimationSpeed.current
    val infiniteTransition = rememberInfiniteTransition(label = "BreathingTransition")
    val cycleDuration = scaledDuration(1700, speed)

    val animatedBorderColor by infiniteTransition.animateColor(
        initialValue = ElectricCyan,
        targetValue = CyberPink,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = cycleDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderColor"
    )

    val animatedStrokeWidth by infiniteTransition.animateFloat(
        initialValue = 1.5f,
        targetValue = 3.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = cycleDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderWidth"
    )

    val coreScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = cycleDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "coreScale"
    )

    MotionCard(
        title = "Glowing Breathing Core",
        apiTag = "RepeatMode.Reverse + animateColor",
        subtitle = "Bi-directional color, scale, and stroke oscillation with ambient halo",
        accentColor = CyberPink
    ) {
        MotionStage(height = 155.dp) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = coreScale * 1.04f
                        scaleY = coreScale * 1.04f
                    }
                    .size(width = 250.dp, height = 92.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(animatedBorderColor.copy(alpha = 0.18f))
            )

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = coreScale
                        scaleY = coreScale
                    }
                    .size(width = 236.dp, height = 80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ObsidianElevated)
                    .border(
                        width = animatedStrokeWidth.dp,
                        color = animatedBorderColor,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "NEON REACTOR CORE",
                        color = animatedBorderColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Stroke: ${String.format(Locale.US, "%.1f", animatedStrokeWidth)}dp • Scale: ${String.format(Locale.US, "%.2f", coreScale)}x",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
