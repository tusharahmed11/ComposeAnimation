package com.imtusharahmed.composeanimation.ui.screens.values

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtusharahmed.composeanimation.ui.common.AnimationSpecSelector
import com.imtusharahmed.composeanimation.ui.common.AnimationSpecType
import com.imtusharahmed.composeanimation.ui.common.LabActionButton
import com.imtusharahmed.composeanimation.ui.common.LocalAnimationSpeed
import com.imtusharahmed.composeanimation.ui.common.MotionCard
import com.imtusharahmed.composeanimation.ui.common.MotionStage
import com.imtusharahmed.composeanimation.ui.common.OptionPillStrip
import com.imtusharahmed.composeanimation.ui.common.TelemetryStrip
import com.imtusharahmed.composeanimation.ui.common.scaledDuration
import com.imtusharahmed.composeanimation.ui.theme.AmberGlow
import com.imtusharahmed.composeanimation.ui.theme.CyberPink
import com.imtusharahmed.composeanimation.ui.theme.CyberViolet
import com.imtusharahmed.composeanimation.ui.theme.ElectricBlue
import com.imtusharahmed.composeanimation.ui.theme.ElectricCyan
import com.imtusharahmed.composeanimation.ui.theme.NeonGreen
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBase
import com.imtusharahmed.composeanimation.ui.theme.ObsidianCard
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.RadiantSunset
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun ValueAnimationsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        FloatAndDpDashboardCard()
        ColorOffsetAndSizeCard()
        DiscreteIntValuesCard()
        CustomTwoWayConverterCard()
    }
}

/* =======================================================================
   1. animateFloatAsState & animateDpAsState Matrix
   ======================================================================= */
@Composable
private fun FloatAndDpDashboardCard() {
    var isActivated by remember { mutableStateOf(false) }
    var selectedSpec by remember { mutableStateOf(AnimationSpecType.SPRING) }
    val speed = LocalAnimationSpeed.current

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isActivated) 1.0f else 0.55f,
        animationSpec = selectedSpec.createSpec(speed, 0.55f, 1.0f),
        label = "alpha"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (isActivated) 1.06f else 0.86f,
        animationSpec = selectedSpec.createSpec(speed, 0.86f, 1.06f),
        label = "scale"
    )
    val animatedRotX by animateFloatAsState(
        targetValue = if (isActivated) 22f else 0f,
        animationSpec = selectedSpec.createSpec(speed, 0f, 22f),
        label = "rotationX"
    )
    val animatedRotZ by animateFloatAsState(
        targetValue = if (isActivated) 8f else -5f,
        animationSpec = selectedSpec.createSpec(speed, -5f, 8f),
        label = "rotationZ"
    )

    val animatedElevation by animateDpAsState(
        targetValue = if (isActivated) 18.dp else 2.dp,
        animationSpec = selectedSpec.createSpec<Dp>(speed, 2.dp, 18.dp),
        label = "elevation"
    )
    val animatedCornerRadius by animateDpAsState(
        targetValue = if (isActivated) 28.dp else 10.dp,
        animationSpec = selectedSpec.createSpec<Dp>(speed, 10.dp, 28.dp),
        label = "cornerRadius"
    )
    val animatedStrokeWidth by animateDpAsState(
        targetValue = if (isActivated) 2.5.dp else 1.dp,
        animationSpec = selectedSpec.createSpec<Dp>(speed, 1.dp, 2.5.dp),
        label = "strokeWidth"
    )

    MotionCard(
        title = "Float & Dp 3D Matrix",
        apiTag = "animateFloatAsState + animateDpAsState",
        subtitle = "Synchronized 3D RotationX/Z, Scale, Alpha, Corner Radius & Dynamic Stroke",
        accentColor = CyberViolet
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AnimationSpecSelector(
                selectedSpec = selectedSpec,
                onSpecSelected = { selectedSpec = it }
            )

            TelemetryStrip(
                items = listOf(
                    "rotX" to "${animatedRotX.toInt()}°",
                    "rotZ" to "${animatedRotZ.toInt()}°",
                    "radius" to "${animatedCornerRadius.value.toInt()}dp"
                ),
                accentColor = ElectricCyan
            )

            MotionStage(height = 170.dp) {
                Box(
                    modifier = Modifier
                        .size(230.dp, 110.dp)
                        .graphicsLayer {
                            alpha = animatedAlpha
                            scaleX = animatedScale
                            scaleY = animatedScale
                            rotationX = animatedRotX
                            rotationZ = animatedRotZ
                            cameraDistance = 12f * density
                        }
                        .shadow(animatedElevation, RoundedCornerShape(animatedCornerRadius))
                        .clip(RoundedCornerShape(animatedCornerRadius))
                        .background(
                            Brush.linearGradient(
                                listOf(ObsidianElevated, CyberViolet.copy(alpha = 0.45f), ObsidianCard)
                            )
                        )
                        .border(animatedStrokeWidth, ElectricCyan, RoundedCornerShape(animatedCornerRadius))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isActivated) "3D FLIGHT ACTIVE" else "RESTING MATRIX",
                            color = ElectricCyan,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "scale: ${String.format(Locale.US, "%.2f", animatedScale)}x • spec: ${selectedSpec.label}",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            LabActionButton(
                text = if (isActivated) "Reset to Resting Matrix" else "Engage 3D Perspective Transform",
                onClick = { isActivated = !isActivated },
                modifier = Modifier.fillMaxWidth(),
                color = if (isActivated) RadiantSunset else ElectricCyan
            )
        }
    }
}

/* =======================================================================
   2. animateColorAsState, animateOffsetAsState, animateSizeAsState, animateRectAsState
   ======================================================================= */
@Composable
private fun ColorOffsetAndSizeCard() {
    var targetMode by remember { mutableIntStateOf(1) }
    val speed = LocalAnimationSpeed.current
    val animDuration = scaledDuration(550, speed)
    val effectiveStiffness = (Spring.StiffnessLow * speed * speed).coerceIn(30f, 50000f)

    val startColor by animateColorAsState(
        targetValue = when (targetMode) {
            0 -> ElectricCyan
            1 -> CyberViolet
            else -> RadiantSunset
        },
        animationSpec = tween(animDuration),
        label = "startColor"
    )
    val endColor by animateColorAsState(
        targetValue = when (targetMode) {
            0 -> ElectricBlue
            1 -> CyberPink
            else -> AmberGlow
        },
        animationSpec = tween(animDuration),
        label = "endColor"
    )

    val targetOffset = when (targetMode) {
        0 -> Offset(-48f, -16f)
        1 -> Offset(0f, 0f)
        else -> Offset(48f, 16f)
    }
    val animatedOffset by animateOffsetAsState(
        targetValue = targetOffset,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = effectiveStiffness),
        label = "offset"
    )

    val targetSize = when (targetMode) {
        0 -> Size(145f, 64f)
        1 -> Size(195f, 82f)
        else -> Size(155f, 72f)
    }
    val animatedSize by animateSizeAsState(
        targetValue = targetSize,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = effectiveStiffness * 1.5f),
        label = "size"
    )

    // Centered Rect bounds halo around the target
    val targetRect = when (targetMode) {
        0 -> Rect(-48f, -16f, 165f, 84f)
        1 -> Rect(0f, 0f, 215f, 102f)
        else -> Rect(48f, 16f, 175f, 92f)
    }
    val animatedRect by animateRectAsState(
        targetValue = targetRect,
        animationSpec = tween(animDuration),
        label = "rect"
    )

    val modes = listOf(0 to "Cyan Left", 1 to "Violet Center", 2 to "Sunset Right")

    MotionCard(
        title = "Color, Offset, Size & Rect",
        apiTag = "animateColor / Offset / Size / Rect",
        subtitle = "Continuous 2D vector kinematics, gradient color interpolation, and outer Rect halo",
        accentColor = ElectricCyan
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OptionPillStrip(
                options = modes,
                selectedOption = modes.first { it.first == targetMode },
                onOptionSelected = { targetMode = it.first },
                labelProvider = { it.second },
                accentColor = ElectricCyan
            )

            MotionStage(height = 165.dp) {
                // Outer animated Rect halo centered around the moving node
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                animatedRect.left.dp.roundToPx(),
                                animatedRect.top.dp.roundToPx()
                            )
                        }
                        .size(animatedRect.right.dp, animatedRect.bottom.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, startColor.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                )

                // Inner animated Offset + Size + Color node
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                animatedOffset.x.dp.roundToPx(),
                                animatedOffset.y.dp.roundToPx()
                            )
                        }
                        .size(animatedSize.width.dp, animatedSize.height.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(startColor, endColor)))
                        .border(1.5.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "XY: (${animatedOffset.x.toInt()}, ${animatedOffset.y.toInt()})",
                            color = ObsidianBase,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "${animatedSize.width.toInt()}×${animatedSize.height.toInt()} dp",
                            color = ObsidianBase.copy(alpha = 0.8f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

/* =======================================================================
   3. animateIntAsState, animateIntOffsetAsState, animateIntSizeAsState
   ======================================================================= */
@Composable
private fun DiscreteIntValuesCard() {
    var intCounter by remember { mutableIntStateOf(1024) }
    var isOffsetToggled by remember { mutableStateOf(false) }
    var isExpandedSize by remember { mutableStateOf(false) }
    val speed = LocalAnimationSpeed.current
    val animDuration = scaledDuration(500, speed)
    val effectiveStiffness = (Spring.StiffnessMedium * speed * speed).coerceIn(40f, 50000f)

    val animatedInt by animateIntAsState(
        targetValue = intCounter,
        animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing),
        label = "intCounter"
    )
    val animatedIntOffset by animateIntOffsetAsState(
        targetValue = if (isOffsetToggled) IntOffset(75, 16) else IntOffset(-40, -10),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = effectiveStiffness),
        label = "intOffset"
    )
    val animatedIntSize by animateIntSizeAsState(
        targetValue = if (isExpandedSize) IntSize(220, 82) else IntSize(155, 58),
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = effectiveStiffness),
        label = "intSize"
    )

    MotionCard(
        title = "Discrete Int Value Suite",
        apiTag = "animateInt / IntOffset / IntSize",
        subtitle = "Pixel-exact discrete integer counters, pixel offsets, and integer bounds",
        accentColor = AmberGlow
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MotionStage(height = 150.dp) {
                Box(
                    modifier = Modifier
                        .offset { animatedIntOffset }
                        .size(animatedIntSize.width.dp, animatedIntSize.height.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(ObsidianElevated)
                        .border(1.5.dp, AmberGlow, RoundedCornerShape(14.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "COUNT: $animatedInt",
                            color = AmberGlow,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${animatedIntSize.width}×${animatedIntSize.height}dp",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LabActionButton(
                    text = "+256 Int",
                    onClick = { intCounter += 256 },
                    modifier = Modifier.weight(1f),
                    color = ElectricCyan
                )
                LabActionButton(
                    text = if (isOffsetToggled) "Shift Left" else "Shift Right",
                    onClick = { isOffsetToggled = !isOffsetToggled },
                    modifier = Modifier.weight(1f),
                    color = CyberViolet
                )
                LabActionButton(
                    text = if (isExpandedSize) "Shrink" else "Expand",
                    onClick = { isExpandedSize = !isExpandedSize },
                    modifier = Modifier.weight(1f),
                    color = AmberGlow
                )
            }
        }
    }
}

/* =======================================================================
   4. Custom TwoWayConverter<MyColorProfile, AnimationVector4D>
   ======================================================================= */
data class MyColorProfile(
    val red: Float,
    val green: Float,
    val blue: Float,
    val luminescence: Float
) {
    fun toComposeColor(): Color =
        Color(
            red = (red * luminescence).coerceIn(0f, 1f),
            green = (green * luminescence).coerceIn(0f, 1f),
            blue = (blue * luminescence).coerceIn(0f, 1f),
            alpha = 1f
        )
}

val ColorProfileConverter = TwoWayConverter<MyColorProfile, AnimationVector4D>(
    convertToVector = { profile ->
        AnimationVector4D(profile.red, profile.green, profile.blue, profile.luminescence)
    },
    convertFromVector = { vector ->
        MyColorProfile(
            red = vector.v1.coerceIn(0f, 1f),
            green = vector.v2.coerceIn(0f, 1f),
            blue = vector.v3.coerceIn(0f, 1f),
            luminescence = vector.v4.coerceIn(0.1f, 2f)
        )
    }
)

@Composable
private fun CustomTwoWayConverterCard() {
    val profiles = remember {
        listOf(
            "Cyan Glow" to MyColorProfile(0.0f, 0.96f, 0.83f, 1.2f),
            "Cyber Violet" to MyColorProfile(0.55f, 0.28f, 0.96f, 1.0f),
            "Sunset Neon" to MyColorProfile(1.0f, 0.16f, 0.52f, 1.4f),
            "Matrix Green" to MyColorProfile(0.15f, 0.96f, 0.53f, 0.95f)
        )
    }
    var selectedProfileIndex by remember { mutableIntStateOf(0) }
    val speed = LocalAnimationSpeed.current
    val animDuration = scaledDuration(650, speed)

    val animatedProfile by animateValueAsState(
        targetValue = profiles[selectedProfileIndex].second,
        typeConverter = ColorProfileConverter,
        animationSpec = tween(durationMillis = animDuration, easing = FastOutSlowInEasing),
        label = "customTwoWayConverter"
    )

    MotionCard(
        title = "Custom TwoWayConverter<T, V>",
        apiTag = "TwoWayConverter + AnimationVector4D",
        subtitle = "Interpolate custom domain data classes across 4 simultaneous vector channels",
        accentColor = CyberPink
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OptionPillStrip(
                options = profiles.indices.toList(),
                selectedOption = selectedProfileIndex,
                onOptionSelected = { selectedProfileIndex = it },
                labelProvider = { profiles[it].first },
                accentColor = animatedProfile.toComposeColor()
            )

            MotionStage(height = 155.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(animatedProfile.toComposeColor())
                            .border(2.dp, Color.White.copy(alpha = 0.75f), RoundedCornerShape(22.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "4D",
                            color = ObsidianBase,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        VectorChannelBar("v1 (R)", animatedProfile.red, 1f, RadiantSunset)
                        VectorChannelBar("v2 (G)", animatedProfile.green, 1f, NeonGreen)
                        VectorChannelBar("v3 (B)", animatedProfile.blue, 1f, ElectricCyan)
                        VectorChannelBar("v4 (Luma)", animatedProfile.luminescence, 1.5f, AmberGlow)
                    }
                }
            }
        }
    }
}

@Composable
private fun VectorChannelBar(
    label: String,
    value: Float,
    maxVal: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = String.format(Locale.US, "%.2f", value),
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(ObsidianElevated)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((value / maxVal).coerceIn(0.02f, 1f))
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}
