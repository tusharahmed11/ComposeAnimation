package com.imtusharahmed.composeanimation.ui.screens.lowlevel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.DecayAnimation
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtusharahmed.composeanimation.ui.common.LabActionButton
import com.imtusharahmed.composeanimation.ui.common.LocalAnimationSpeed
import com.imtusharahmed.composeanimation.ui.common.MotionCard
import com.imtusharahmed.composeanimation.ui.common.MotionStage
import com.imtusharahmed.composeanimation.ui.common.TelemetryStrip
import com.imtusharahmed.composeanimation.ui.common.scaledDuration
import com.imtusharahmed.composeanimation.ui.theme.AmberGlow
import com.imtusharahmed.composeanimation.ui.theme.CyberPink
import com.imtusharahmed.composeanimation.ui.theme.CyberViolet
import com.imtusharahmed.composeanimation.ui.theme.ElectricCyan
import com.imtusharahmed.composeanimation.ui.theme.NeonGreen
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBase
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorder
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.RadiantSunset
import com.imtusharahmed.composeanimation.ui.theme.TextPrimary
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LowLevelEnginesScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AnimatableEngineCard()
        TargetBasedAnimationCard()
        DecayAnimationCard()
    }
}

/* =======================================================================
   1. Animatable: Sequential Chaining, snapTo, updateBounds, Velocity Handoff
   ======================================================================= */
@Composable
private fun AnimatableEngineCard() {
    val coroutineScope = rememberCoroutineScope()
    val speed = LocalAnimationSpeed.current
    val animatableX = remember { Animatable(0f) }
    var boundsEnabled by remember { mutableStateOf(false) }
    var activeJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(boundsEnabled) {
        if (boundsEnabled) {
            animatableX.updateBounds(lowerBound = -65f, upperBound = 65f)
        } else {
            animatableX.updateBounds(lowerBound = null, upperBound = null)
        }
    }

    MotionCard(
        title = "Animatable Imperative Engine",
        apiTag = "Animatable + animateTo / snapTo / updateBounds",
        subtitle = "Coroutine-driven sequential chaining, hard bounds clamping, and mid-flight velocity diversion",
        accentColor = CyberPink
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TelemetryStrip(
                items = listOf(
                    "Pos" to "${String.format(Locale.US, "%.1f", animatableX.value)}dp",
                    "Vel" to "${String.format(Locale.US, "%.0f", animatableX.velocity)}dp/s",
                    "Bounds" to if (boundsEnabled) "±65dp" else "FREE"
                ),
                accentColor = ElectricCyan
            )

            MotionStage(height = 140.dp) {
                // Horizontal track rail
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.82f)
                        .height(2.dp)
                        .background(ObsidianBorder)
                )

                if (boundsEnabled) {
                    Box(
                        modifier = Modifier
                            .width(178.dp)
                            .height(64.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(RadiantSunset.copy(alpha = 0.08f))
                            .border(1.5.dp, RadiantSunset.copy(alpha = 0.75f), RoundedCornerShape(14.dp))
                    )
                }

                Box(
                    modifier = Modifier
                        .offset { IntOffset(animatableX.value.dp.roundToPx(), 0) }
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(ElectricCyan, CyberViolet)
                            )
                        )
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color.White))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LabActionButton(
                    text = "1. Chain",
                    onClick = {
                        activeJob?.cancel()
                        activeJob = coroutineScope.launch {
                            val dur = scaledDuration(380, speed)
                            val stiff = (Spring.StiffnessMedium * speed * speed).coerceIn(40f, 50000f)
                            animatableX.animateTo(100f, tween(dur))
                            animatableX.animateTo(-100f, spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = stiff))
                            animatableX.animateTo(0f, tween(dur))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    color = ElectricCyan
                )

                LabActionButton(
                    text = "2. Snap 0",
                    onClick = {
                        activeJob?.cancel()
                        activeJob = coroutineScope.launch {
                            animatableX.snapTo(0f)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    color = RadiantSunset
                )

                LabActionButton(
                    text = "3. Divert",
                    onClick = {
                        activeJob?.cancel()
                        activeJob = coroutineScope.launch {
                            val flight = launch {
                                animatableX.animateTo(115f, tween(scaledDuration(1000, speed)))
                            }
                            delay(scaledDuration(280, speed).toLong())
                            flight.cancel()
                            val currentVel = animatableX.velocity
                            animatableX.animateTo(
                                targetValue = -115f,
                                initialVelocity = currentVel,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = (Spring.StiffnessMedium * speed * speed).coerceIn(40f, 50000f)
                                )
                            )
                            animatableX.animateTo(0f, tween(scaledDuration(350, speed)))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    color = AmberGlow
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ObsidianElevated)
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
                    .clickable { boundsEnabled = !boundsEnabled }
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hard Clamp Bounds [-65dp, +65dp]",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (boundsEnabled) "ENABLED ●" else "DISABLED ○",
                    color = if (boundsEnabled) RadiantSunset else TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/* =======================================================================
   2. TargetBasedAnimation: Manual Frame-by-Frame withFrameNanos Render Loop
   ======================================================================= */
@Composable
private fun TargetBasedAnimationCard() {
    val speed = LocalAnimationSpeed.current
    var isRunning by remember { mutableStateOf(true) }
    var currentAngle by remember { mutableFloatStateOf(0f) }
    var frameDeltaNanos by remember { mutableLongStateOf(0L) }
    var totalFramesRendered by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isRunning, speed) {
        if (!isRunning) return@LaunchedEffect
        val animDurationNanos = (2_000_000_000L / speed).toLong().coerceAtLeast(50_000_000L)
        val animation = TargetBasedAnimation(
            animationSpec = tween(durationMillis = scaledDuration(2000, speed)),
            typeConverter = Float.VectorConverter,
            initialValue = 0f,
            targetValue = 360f
        )

        var startTimeNanos = 0L
        var lastFrameNanos = 0L
        while (isActive && isRunning) {
            withFrameNanos { frameTimeNanos ->
                if (startTimeNanos == 0L) {
                    startTimeNanos = frameTimeNanos
                    lastFrameNanos = frameTimeNanos
                }
                frameDeltaNanos = frameTimeNanos - lastFrameNanos
                lastFrameNanos = frameTimeNanos
                totalFramesRendered++

                val playTimeNanos = (frameTimeNanos - startTimeNanos) % animDurationNanos
                currentAngle = animation.getValueFromNanos(playTimeNanos)
            }
        }
    }

    MotionCard(
        title = "TargetBasedAnimation Clock",
        apiTag = "TargetBasedAnimation + withFrameNanos",
        subtitle = "Explicit nanosecond vsync frame loop evaluating animation vectors per display tick",
        accentColor = ElectricCyan
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TelemetryStrip(
                items = listOf(
                    "Angle" to "${currentAngle.toInt()}°",
                    "Δt" to "${String.format(Locale.US, "%.1f", frameDeltaNanos / 1_000_000f)}ms",
                    "Frames" to "$totalFramesRendered"
                ),
                accentColor = NeonGreen
            )

            MotionStage(height = 170.dp) {
                Canvas(modifier = Modifier.size(136.dp)) {
                    val radius = size.minDimension / 2.2f
                    val center = Offset(size.width / 2, size.height / 2)

                    drawCircle(color = ObsidianBorder, radius = radius, style = Stroke(width = 1.5.dp.toPx()))
                    drawCircle(color = ObsidianBorder.copy(alpha = 0.45f), radius = radius * 0.55f, style = Stroke(width = 1.dp.toPx()))

                    // Glowing orbital trail arc
                    drawArc(
                        color = ElectricCyan.copy(alpha = 0.45f),
                        startAngle = currentAngle - 65f,
                        sweepAngle = 65f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )

                    val angleRad = (currentAngle * PI / 180f).toFloat()
                    val nodeX = center.x + radius * cos(angleRad)
                    val nodeY = center.y + radius * sin(angleRad)

                    drawLine(
                        color = ElectricCyan.copy(alpha = 0.4f),
                        start = center,
                        end = Offset(nodeX, nodeY),
                        strokeWidth = 2.dp.toPx()
                    )

                    drawCircle(color = CyberViolet, radius = 6.dp.toPx(), center = center)
                    drawCircle(color = ElectricCyan.copy(alpha = 0.3f), radius = 13.dp.toPx(), center = Offset(nodeX, nodeY))
                    drawCircle(color = ElectricCyan, radius = 7.dp.toPx(), center = Offset(nodeX, nodeY))
                    drawCircle(color = Color.White, radius = 3.dp.toPx(), center = Offset(nodeX, nodeY))
                }
            }

            LabActionButton(
                text = if (isRunning) "Pause withFrameNanos Clock ❚❚" else "Resume withFrameNanos Clock ▶",
                onClick = { isRunning = !isRunning },
                modifier = Modifier.fillMaxWidth(),
                color = if (isRunning) RadiantSunset else ElectricCyan
            )
        }
    }
}

/* =======================================================================
   3. DecayAnimation: Physical Scroll Wheel / Roulette (Exponential & Spline)
   ======================================================================= */
@Composable
private fun DecayAnimationCard() {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    var currentAngle by remember { mutableFloatStateOf(0f) }
    var currentVelocity by remember { mutableFloatStateOf(0f) }
    var useSplineDecay by remember { mutableStateOf(false) }
    var activeFlingJob by remember { mutableStateOf<Job?>(null) }

    fun triggerFling(velocity: Float) {
        activeFlingJob?.cancel()
        activeFlingJob = coroutineScope.launch {
            val spec = if (useSplineDecay) {
                splineBasedDecay<Float>(density)
            } else {
                exponentialDecay<Float>(frictionMultiplier = 0.75f)
            }

            val decayAnim = DecayAnimation(
                animationSpec = spec,
                typeConverter = Float.VectorConverter,
                initialValue = currentAngle,
                initialVelocity = velocity
            )

            var startTimeNanos = 0L
            while (isActive) {
                withFrameNanos { frameTimeNanos ->
                    if (startTimeNanos == 0L) startTimeNanos = frameTimeNanos
                    val playTimeNanos = frameTimeNanos - startTimeNanos
                    currentAngle = decayAnim.getValueFromNanos(playTimeNanos)
                    currentVelocity = decayAnim.getVelocityVectorFromNanos(playTimeNanos).value

                    if (decayAnim.isFinishedFromNanos(playTimeNanos)) {
                        currentVelocity = 0f
                        activeFlingJob = null
                    }
                }
                if (activeFlingJob == null) break
            }
        }
    }

    MotionCard(
        title = "DecayAnimation Ballistics",
        apiTag = "exponentialDecay + splineBasedDecay",
        subtitle = "Targetless inertial momentum & friction deceleration from initial fling velocity",
        accentColor = AmberGlow
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TelemetryStrip(
                    items = listOf(
                        "Angle" to "${(currentAngle % 360).toInt()}°",
                        "ω" to "${currentVelocity.toInt()}°/s"
                    ),
                    accentColor = AmberGlow,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ObsidianElevated)
                        .border(1.dp, CyberViolet, RoundedCornerShape(10.dp))
                        .clickable { useSplineDecay = !useSplineDecay }
                        .padding(horizontal = 10.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = if (useSplineDecay) "Spline" else "Expon.",
                        color = TextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            MotionStage(height = 175.dp) {
                Box(
                    modifier = Modifier
                        .size(132.dp)
                        .graphicsLayer { rotationZ = currentAngle }
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(ElectricCyan, CyberViolet, RadiantSunset, AmberGlow, NeonGreen, ElectricCyan)
                            )
                        )
                        .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(132.dp)) {
                        for (i in 0 until 8) {
                            val angleRad = (i * 45 * PI / 180f).toFloat()
                            val endX = center.x + (size.minDimension / 2f) * cos(angleRad)
                            val endY = center.y + (size.minDimension / 2f) * sin(angleRad)
                            drawLine(
                                color = ObsidianBase,
                                start = center,
                                end = Offset(endX, endY),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                        drawCircle(color = ObsidianBase, radius = 22.dp.toPx())
                        drawCircle(color = ElectricCyan, radius = 8.dp.toPx())
                    }
                }

                // Top Indicator Needle
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp)
                        .size(width = 6.dp, height = 20.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LabActionButton(
                    text = "Spin +1.5k",
                    onClick = { triggerFling(1500f) },
                    modifier = Modifier.weight(1f),
                    color = ElectricCyan
                )
                LabActionButton(
                    text = "Turbo +4.2k",
                    onClick = { triggerFling(4200f) },
                    modifier = Modifier.weight(1f),
                    color = CyberViolet
                )
                LabActionButton(
                    text = "Rev -2.8k",
                    onClick = { triggerFling(-2800f) },
                    modifier = Modifier.weight(1f),
                    color = RadiantSunset
                )
            }
        }
    }
}
