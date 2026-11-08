package com.imtusharahmed.composeanimation.ui.screens.specs

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtusharahmed.composeanimation.ui.common.LabActionButton
import com.imtusharahmed.composeanimation.ui.common.LocalAnimationSpeed
import com.imtusharahmed.composeanimation.ui.common.MotionCard
import com.imtusharahmed.composeanimation.ui.common.OptionPillStrip
import com.imtusharahmed.composeanimation.ui.common.scaledDuration
import com.imtusharahmed.composeanimation.ui.theme.AmberGlow
import com.imtusharahmed.composeanimation.ui.theme.CyberViolet
import com.imtusharahmed.composeanimation.ui.theme.ElectricBlue
import com.imtusharahmed.composeanimation.ui.theme.ElectricCyan
import com.imtusharahmed.composeanimation.ui.theme.NeonGreen
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBase
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorderSubtle
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.RadiantSunset
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun AnimationSpecGalleryScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        SpringComparativeVisualizerCard()
        TweenEasingsComparativeCard()
        KeyframesRepeatableAndSnapCard()
    }
}

/* =======================================================================
   1. Spring Comparative Visualizer: All Damping Ratios & Stiffness Values
   ======================================================================= */
@Composable
private fun SpringComparativeVisualizerCard() {
    var isToggled by remember { mutableStateOf(false) }
    var selectedStiffness by remember { mutableFloatStateOf(Spring.StiffnessLow) }
    val speed = LocalAnimationSpeed.current

    val effectiveStiffness = (selectedStiffness * speed * speed).coerceIn(20f, 50000f)
    val targetProgress = if (isToggled) 1f else 0f

    val highBouncy by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = effectiveStiffness),
        label = "highBouncy"
    )
    val mediumBouncy by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = effectiveStiffness),
        label = "mediumBouncy"
    )
    val lowBouncy by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = effectiveStiffness),
        label = "lowBouncy"
    )
    val noBouncy by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = effectiveStiffness),
        label = "noBouncy"
    )

    val stiffnessOptions = remember {
        listOf(
            Spring.StiffnessVeryLow to "VeryLow (50)",
            Spring.StiffnessLow to "Low (200)",
            Spring.StiffnessMedium to "Medium (1.5k)",
            Spring.StiffnessHigh to "High (10k)"
        )
    }

    MotionCard(
        title = "Spring Physics Gallery",
        apiTag = "spring(dampingRatio, stiffness)",
        subtitle = "Side-by-side harmonic comparison across all 4 damping ratios with unclipped overshoot zones",
        accentColor = AmberGlow
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OptionPillStrip(
                options = stiffnessOptions,
                selectedOption = stiffnessOptions.first { it.first == selectedStiffness },
                onOptionSelected = { selectedStiffness = it.first },
                labelProvider = { it.second },
                accentColor = AmberGlow
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianBase)
                    .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(
                    Triple("HighBouncy (ζ = 0.2)", highBouncy, RadiantSunset),
                    Triple("MediumBouncy (ζ = 0.5)", mediumBouncy, AmberGlow),
                    Triple("LowBouncy (ζ = 0.75)", lowBouncy, ElectricCyan),
                    Triple("NoBouncy (ζ = 1.0)", noBouncy, NeonGreen)
                ).forEach { (title, progress, color) ->
                    NormalizedTrackLane(
                        title = title,
                        progress = progress,
                        color = color
                    )
                }
            }

            LabActionButton(
                text = if (isToggled) "Release Springs ← (Return to 0%)" else "Launch Springs Simultaneously → (100%)",
                onClick = { isToggled = !isToggled },
                modifier = Modifier.fillMaxWidth(),
                color = if (isToggled) RadiantSunset else ElectricCyan
            )
        }
    }
}

/* =======================================================================
   2. Tween & Easings Comparative Card: 5 Distinct Curves
   ======================================================================= */
@Composable
private fun TweenEasingsComparativeCard() {
    var isRacing by remember { mutableStateOf(false) }
    val speed = LocalAnimationSpeed.current
    val animDuration = scaledDuration(1200, speed)

    val targetProgress = if (isRacing) 1f else 0f

    val fastOutSlowIn by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing),
        label = "fastOutSlowIn"
    )
    val linearOutSlowIn by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = animDuration, easing = LinearOutSlowInEasing),
        label = "linearOutSlowIn"
    )
    val fastOutLinearIn by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = animDuration, easing = FastOutLinearInEasing),
        label = "fastOutLinearIn"
    )
    val linearEasing by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = animDuration, easing = LinearEasing),
        label = "linearEasing"
    )
    val customCubicBezier by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = animDuration, easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1.0f)),
        label = "customCubicBezier"
    )

    MotionCard(
        title = "Tween & Easing Curves Race",
        apiTag = "tween(durationMillis, easing)",
        subtitle = "Compare standard Material easings against Linear and an overshooting CubicBezier curve",
        accentColor = ElectricBlue
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianBase)
                    .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                listOf(
                    Triple("FastOutSlowIn (Standard)", fastOutSlowIn, ElectricCyan),
                    Triple("LinearOutSlowIn (Decelerate)", linearOutSlowIn, ElectricBlue),
                    Triple("FastOutLinearIn (Accelerate)", fastOutLinearIn, AmberGlow),
                    Triple("LinearEasing (Constant)", linearEasing, TextSecondary),
                    Triple("CubicBezier(0.34, 1.56, 0.64, 1.0)", customCubicBezier, RadiantSunset)
                ).forEach { (title, progress, color) ->
                    NormalizedTrackLane(
                        title = title,
                        progress = progress,
                        color = color
                    )
                }
            }

            LabActionButton(
                text = if (isRacing) "Reset Easing Race ← (0%)" else "Start 5-Lane Easing Race → (100%)",
                onClick = { isRacing = !isRacing },
                modifier = Modifier.fillMaxWidth(),
                color = ElectricBlue
            )
        }
    }
}

/* =======================================================================
   3. Keyframes, Repeatable vs InfiniteRepeatable, and Snap Specs
   ======================================================================= */
@Composable
private fun KeyframesRepeatableAndSnapCard() {
    val speed = LocalAnimationSpeed.current
    var isKeyframesTriggered by remember { mutableStateOf(false) }
    var repeatableTrigger by remember { mutableStateOf(false) }
    var isSnapTriggered by remember { mutableStateOf(false) }
    val totalKeyframeTime = scaledDuration(1100, speed)

    val keyframesProgress by animateFloatAsState(
        targetValue = if (isKeyframesTriggered) 1f else 0f,
        animationSpec = keyframes {
            durationMillis = totalKeyframeTime
            if (isKeyframesTriggered) {
                0f at 0 using LinearEasing
                0.35f at (totalKeyframeTime * 0.25f).toInt() using FastOutSlowInEasing
                0.85f at (totalKeyframeTime * 0.55f).toInt() using CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f)
                0.62f at (totalKeyframeTime * 0.78f).toInt() using LinearOutSlowInEasing
                1f at totalKeyframeTime
            } else {
                1f at 0 using LinearEasing
                0.65f at (totalKeyframeTime * 0.25f).toInt() using FastOutSlowInEasing
                0.15f at (totalKeyframeTime * 0.55f).toInt() using CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f)
                0.38f at (totalKeyframeTime * 0.78f).toInt() using LinearOutSlowInEasing
                0f at totalKeyframeTime
            }
        },
        label = "keyframesOffset"
    )

    val repeatableProgress by animateFloatAsState(
        targetValue = if (repeatableTrigger) 1f else 0f,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(scaledDuration(380, speed), easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "repeatableOffset"
    )

    val snapProgress by animateFloatAsState(
        targetValue = if (isSnapTriggered) 1f else 0f,
        animationSpec = snap(delayMillis = scaledDuration(150, speed)),
        label = "snapOffset"
    )

    MotionCard(
        title = "Keyframes, Repeatable & Snap",
        apiTag = "keyframes / repeatable / snap",
        subtitle = "Multi-waypoint paths with mid-flight pullback, finite loop iterations, and instant step jumps",
        accentColor = CyberViolet
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianBase)
                    .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NormalizedTrackLane(
                    title = "1. Keyframes (5-waypoint path with pullback)",
                    progress = keyframesProgress,
                    color = ElectricCyan
                )
                NormalizedTrackLane(
                    title = "2. Repeatable (3× Reverse cycles)",
                    progress = repeatableProgress,
                    color = CyberViolet
                )
                NormalizedTrackLane(
                    title = "3. Snap (Instant jump after 150ms delay)",
                    progress = snapProgress,
                    color = RadiantSunset
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LabActionButton(
                    text = "1. Keyframes",
                    onClick = { isKeyframesTriggered = !isKeyframesTriggered },
                    modifier = Modifier.weight(1f),
                    color = ElectricCyan
                )
                LabActionButton(
                    text = "2. Repeat ×3",
                    onClick = { repeatableTrigger = !repeatableTrigger },
                    modifier = Modifier.weight(1f),
                    color = CyberViolet
                )
                LabActionButton(
                    text = "3. Snap",
                    onClick = { isSnapTriggered = !isSnapTriggered },
                    modifier = Modifier.weight(1f),
                    color = RadiantSunset
                )
            }
        }
    }
}

/**
 * Reusable normalized [0f..1f] track lane that leaves margin on both ends so
 * spring and cubic-bezier overshoots (both < 0f and > 1f) are never clipped.
 */
@Composable
private fun NormalizedTrackLane(
    title: String,
    progress: Float,
    color: Color
) {
    val density = LocalDensity.current
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${(progress * 100).roundToInt()}%",
                color = TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(ObsidianElevated)
        ) {
            val orbSize = 16.dp
            val sideInset = 24.dp
            val travelRangePx = with(density) { (maxWidth - orbSize - sideInset * 2).toPx().coerceAtLeast(1f) }
            val startPx = with(density) { sideInset.toPx() }
            val currentX = (startPx + progress * travelRangePx).roundToInt()

            // Start (0%) and Target (100%) marker lines
            Box(
                modifier = Modifier
                    .offset(x = sideInset + orbSize / 2)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.White.copy(alpha = 0.2f))
            )
            Box(
                modifier = Modifier
                    .offset(x = maxWidth - sideInset - orbSize / 2)
                    .width(1.5.dp)
                    .fillMaxHeight()
                    .background(color.copy(alpha = 0.45f))
            )

            // Active progress trail
            val fillWidthDp = with(density) { (currentX + orbSize.toPx() / 2).toDp().coerceIn(0.dp, maxWidth) }
            Box(
                modifier = Modifier
                    .width(fillWidthDp)
                    .fillMaxHeight()
                    .background(color.copy(alpha = 0.16f))
            )

            // Animated Orb
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset { IntOffset(currentX, 0) }
                    .size(orbSize)
                    .clip(CircleShape)
                    .background(color)
                    .border(1.5.dp, Color.White, CircleShape)
            )
        }
    }
}
