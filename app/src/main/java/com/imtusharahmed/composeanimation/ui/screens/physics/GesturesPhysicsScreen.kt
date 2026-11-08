package com.imtusharahmed.composeanimation.ui.screens.physics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
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
import com.imtusharahmed.composeanimation.ui.common.OptionPillStrip
import com.imtusharahmed.composeanimation.ui.common.TelemetryStrip
import com.imtusharahmed.composeanimation.ui.common.scaledDuration
import com.imtusharahmed.composeanimation.ui.theme.AmberGlow
import com.imtusharahmed.composeanimation.ui.theme.CyberViolet
import com.imtusharahmed.composeanimation.ui.theme.ElectricCyan
import com.imtusharahmed.composeanimation.ui.theme.NeonGreen
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBase
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorder
import com.imtusharahmed.composeanimation.ui.theme.ObsidianCard
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.RadiantSunset
import com.imtusharahmed.composeanimation.ui.theme.TextMuted
import com.imtusharahmed.composeanimation.ui.theme.TextPrimary
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

@Composable
fun GesturesPhysicsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        SwipeableDismissCard()
        AnchoredDraggableDrawerCard()
    }
}

/* =======================================================================
   1. PointerInput with Animatable: Swipeable Dismiss & Spring Snapback
   ======================================================================= */
@Composable
private fun SwipeableDismissCard() {
    val coroutineScope = rememberCoroutineScope()
    val speed = LocalAnimationSpeed.current
    val offsetX = remember { Animatable(0f) }
    var isDismissed by remember { mutableStateOf(false) }
    var lastVelocity by remember { mutableFloatStateOf(0f) }
    val threshold = 170f

    MotionCard(
        title = "Swipe-to-Dismiss Physics",
        apiTag = "pointerInput + VelocityTracker + Animatable",
        subtitle = "Touch-tracked horizontal drag with velocity handoff & bouncy spring snapback",
        accentColor = RadiantSunset
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TelemetryStrip(
                items = listOf(
                    "Offset" to "${offsetX.value.toInt()} px",
                    "Velocity" to "${lastVelocity.toInt()} px/s",
                    "Status" to if (isDismissed) "DISMISSED" else if (abs(offsetX.value) > threshold) "RELEASE!" else "READY"
                ),
                accentColor = if (abs(offsetX.value) > threshold) RadiantSunset else ElectricCyan
            )

            MotionStage(height = 155.dp) {
                val nearThreshold = abs(offsetX.value) > threshold * 0.65f
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "← DISMISS",
                        color = RadiantSunset.copy(alpha = if (nearThreshold && offsetX.value < 0) 0.95f else 0.35f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "DISMISS →",
                        color = RadiantSunset.copy(alpha = if (nearThreshold && offsetX.value > 0) 0.95f else 0.35f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                if (!isDismissed) {
                    val velocityTracker = remember { VelocityTracker() }

                    Box(
                        modifier = Modifier
                            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                            .graphicsLayer {
                                rotationZ = (offsetX.value / 24f).coerceIn(-16f, 16f)
                                val scale = (1f - (abs(offsetX.value) / 1400f)).coerceIn(0.88f, 1f)
                                scaleX = scale
                                scaleY = scale
                                alpha = (1f - (abs(offsetX.value) / 500f)).coerceIn(0.25f, 1f)
                            }
                            .size(width = 230.dp, height = 92.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(ObsidianElevated, CyberViolet.copy(alpha = 0.45f), ObsidianCard)
                                )
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (abs(offsetX.value) > threshold) RadiantSunset else ElectricCyan,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .pointerInput(speed) {
                                detectHorizontalDragGestures(
                                    onDragStart = { velocityTracker.resetTracking() },
                                    onHorizontalDrag = { change, dragAmount ->
                                        change.consume()
                                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                                        coroutineScope.launch {
                                            offsetX.snapTo(offsetX.value + dragAmount)
                                        }
                                    },
                                    onDragEnd = {
                                        val velocity = velocityTracker.calculateVelocity().x
                                        lastVelocity = velocity
                                        coroutineScope.launch {
                                            val flingThreshold = 850f
                                            if (abs(offsetX.value) > threshold || abs(velocity) > flingThreshold) {
                                                val dir = sign(if (abs(velocity) > flingThreshold) velocity else offsetX.value)
                                                val target = (if (dir == 0f) 1f else dir) * 650f
                                                offsetX.animateTo(target, tween(scaledDuration(260, speed)))
                                                isDismissed = true
                                            } else {
                                                offsetX.animateTo(
                                                    targetValue = 0f,
                                                    initialVelocity = velocity,
                                                    animationSpec = spring(
                                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                                        stiffness = (Spring.StiffnessMedium * speed * speed).coerceIn(40f, 50000f)
                                                    )
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "DRAG OR FLING CARD",
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Release early to spring-snap • Fling to dismiss",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Card Flung Off-Stage!",
                            color = RadiantSunset,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LabActionButton(
                            text = "Respawn Card ↺",
                            onClick = {
                                coroutineScope.launch {
                                    isDismissed = false
                                    offsetX.snapTo(0f)
                                    lastVelocity = 0f
                                }
                            },
                            color = ElectricCyan
                        )
                    }
                }
            }
        }
    }
}

/* =======================================================================
   2. AnchoredDraggable: Multi-Anchor Expandable Bottom Sheet / Drawer
   ======================================================================= */
enum class DrawerAnchor(val label: String) {
    COLLAPSED("Collapsed (Bottom)"),
    HALF_EXPANDED("Half (50%)"),
    FULLY_EXPANDED("Expanded (Top)")
}

@Composable
private fun AnchoredDraggableDrawerCard() {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    // Proper bottom-sheet coordinates inside a 230.dp tall stage:
    // FULLY_EXPANDED = near top (16.dp), HALF_EXPANDED = middle (94.dp), COLLAPSED = bottom peek (164.dp)
    val fullTopPx = with(density) { 16.dp.toPx() }
    val halfMidPx = with(density) { 94.dp.toPx() }
    val collapsedBottomPx = with(density) { 164.dp.toPx() }

    val draggableAnchors = remember(density) {
        DraggableAnchors {
            DrawerAnchor.FULLY_EXPANDED at fullTopPx
            DrawerAnchor.HALF_EXPANDED at halfMidPx
            DrawerAnchor.COLLAPSED at collapsedBottomPx
        }
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DrawerAnchor.HALF_EXPANDED,
            anchors = draggableAnchors
        )
    }

    LaunchedEffect(draggableAnchors) {
        state.updateAnchors(draggableAnchors)
    }

    MotionCard(
        title = "AnchoredDraggable Bottom Sheet",
        apiTag = "AnchoredDraggableState + DraggableAnchors",
        subtitle = "Drag sheet up or down across 3 positional snap anchors (Top, Half, Bottom)",
        accentColor = ElectricCyan
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OptionPillStrip(
                options = DrawerAnchor.entries,
                selectedOption = state.currentValue,
                onOptionSelected = { anchor ->
                    coroutineScope.launch {
                        state.animateTo(anchor)
                    }
                },
                labelProvider = { it.label },
                accentColor = ElectricCyan
            )

            MotionStage(
                height = 230.dp,
                contentAlignment = Alignment.TopCenter
            ) {
                // Background Host Content revealed when sheet is collapsed
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "UNDERLYING CANVAS VIEWPORT",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pull the sheet handle upwards to expand telemetry details, or drag down to collapse.",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                val currentOffset = if (state.offset.isNaN()) halfMidPx else state.requireOffset()

                // Interactive Bottom Sheet
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .offset { IntOffset(0, currentOffset.roundToInt()) }
                        .anchoredDraggable(
                            state = state,
                            orientation = Orientation.Vertical
                        )
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(ObsidianElevated, ObsidianCard)
                            )
                        )
                        .border(
                            1.5.dp,
                            ElectricCyan.copy(alpha = 0.8f),
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 14.dp, bottomEnd = 14.dp)
                        )
                        .padding(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Drag Pill Handle
                        Box(
                            modifier = Modifier
                                .size(width = 42.dp, height = 4.dp)
                                .clip(CircleShape)
                                .background(ElectricCyan)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ANCHORED TELEMETRY SHEET",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ElectricCyan.copy(alpha = 0.18f))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Y: ${currentOffset.roundToInt()}px",
                                    color = ElectricCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Content inside the sheet revealed when dragged to HALF or FULL
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SheetMetricTile(
                                label = "ANCHOR",
                                value = state.currentValue.name.substringBefore("_"),
                                color = NeonGreen,
                                modifier = Modifier.weight(1f)
                            )
                            SheetMetricTile(
                                label = "TARGET",
                                value = state.targetValue.name.substringBefore("_"),
                                color = CyberViolet,
                                modifier = Modifier.weight(1f)
                            )
                            SheetMetricTile(
                                label = "SOLVER",
                                value = "SPRING",
                                color = AmberGlow,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ObsidianBase)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Full Expansion Zone: AnchoredDraggable smoothly settles to the nearest positional anchor based on drag velocity and positional thresholds.",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SheetMetricTile(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ObsidianBase)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = value,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}
