package com.imtusharahmed.composeanimation.ui.screens.transitions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.TextMuted
import com.imtusharahmed.composeanimation.ui.theme.TextPrimary
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary

@Composable
fun StateMachinesScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        MediaPlayerTransitionCard()
        ChildTransitionCard()
        TransitionExtensionsCard()
    }
}

/* =======================================================================
   1. updateTransition: Multi-State Media Player (Idle, Buffering, Playing, Paused)
   ====================================================================== */
enum class MediaPlayerState(val label: String) {
    IDLE("Idle"),
    BUFFERING("Buffering"),
    PLAYING("Playing"),
    PAUSED("Paused")
}

@Composable
private fun MediaPlayerTransitionCard() {
    var playerState by remember { mutableStateOf(MediaPlayerState.PLAYING) }
    val speed = LocalAnimationSpeed.current
    val animDuration = scaledDuration(450, speed)
    val effectiveStiffness = (Spring.StiffnessMedium * speed * speed).coerceIn(40f, 50000f)

    val transition = updateTransition(targetState = playerState, label = "MediaPlayerStateMachine")

    val buttonSize by transition.animateDp(
        transitionSpec = {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = effectiveStiffness)
        },
        label = "btnSize"
    ) { state ->
        when (state) {
            MediaPlayerState.IDLE -> 58.dp
            MediaPlayerState.BUFFERING -> 68.dp
            MediaPlayerState.PLAYING -> 78.dp
            MediaPlayerState.PAUSED -> 64.dp
        }
    }

    val cornerRadius by transition.animateDp(
        transitionSpec = { tween(durationMillis = animDuration) },
        label = "cornerRadius"
    ) { state ->
        when (state) {
            MediaPlayerState.IDLE -> 29.dp
            MediaPlayerState.BUFFERING -> 34.dp
            MediaPlayerState.PLAYING -> 20.dp
            MediaPlayerState.PAUSED -> 24.dp
        }
    }

    val primaryColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = animDuration) },
        label = "primaryColor"
    ) { state ->
        when (state) {
            MediaPlayerState.IDLE -> TextMuted
            MediaPlayerState.BUFFERING -> AmberGlow
            MediaPlayerState.PLAYING -> ElectricCyan
            MediaPlayerState.PAUSED -> CyberViolet
        }
    }

    val glowAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = animDuration) },
        label = "glowAlpha"
    ) { state ->
        when (state) {
            MediaPlayerState.IDLE -> 0.12f
            MediaPlayerState.BUFFERING -> 0.45f
            MediaPlayerState.PLAYING -> 0.85f
            MediaPlayerState.PAUSED -> 0.35f
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "mediaLoop")
    val continuousSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(scaledDuration(900, speed), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )
    val eqWave by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(scaledDuration(420, speed), easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eqWave"
    )

    MotionCard(
        title = "updateTransition State Machine",
        apiTag = "updateTransition",
        subtitle = "Multi-property media controller synchronizing Dp, Color, and Canvas iconography",
        accentColor = NeonGreen
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OptionPillStrip(
                options = MediaPlayerState.entries,
                selectedOption = playerState,
                onOptionSelected = { playerState = it },
                labelProvider = { it.label },
                accentColor = primaryColor
            )

            TelemetryStrip(
                items = listOf(
                    "State" to playerState.label,
                    "Size" to "${buttonSize.value.toInt()}dp",
                    "Corner" to "${cornerRadius.value.toInt()}dp"
                ),
                accentColor = primaryColor
            )

            MotionStage(height = 165.dp) {
                // Outer ambient halo
                Box(
                    modifier = Modifier
                        .size(buttonSize + 34.dp)
                        .clip(RoundedCornerShape(cornerRadius + 14.dp))
                        .background(primaryColor.copy(alpha = glowAlpha * 0.22f))
                )

                // Morphing Button Core
                Box(
                    modifier = Modifier
                        .size(buttonSize)
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(ObsidianElevated)
                        .border(2.dp, primaryColor, RoundedCornerShape(cornerRadius))
                        .clickable {
                            val nextOrdinal = (playerState.ordinal + 1) % MediaPlayerState.entries.size
                            playerState = MediaPlayerState.entries[nextOrdinal]
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(26.dp)) {
                        val w = size.width
                        val h = size.height
                        when (playerState) {
                            MediaPlayerState.IDLE -> {
                                val path = Path().apply {
                                    moveTo(w * 0.25f, h * 0.18f)
                                    lineTo(w * 0.82f, h * 0.5f)
                                    lineTo(w * 0.25f, h * 0.82f)
                                    close()
                                }
                                drawPath(path = path, color = primaryColor)
                            }
                            MediaPlayerState.BUFFERING -> {
                                drawArc(
                                    color = primaryColor,
                                    startAngle = continuousSpin,
                                    sweepAngle = 250f,
                                    useCenter = false,
                                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            MediaPlayerState.PLAYING -> {
                                val barW = w * 0.2f
                                val h1 = h * (0.35f + 0.55f * eqWave)
                                val h2 = h * (0.9f - 0.5f * eqWave)
                                val h3 = h * (0.45f + 0.45f * eqWave)
                                drawRoundRect(
                                    color = primaryColor,
                                    topLeft = Offset(w * 0.1f, (h - h1) / 2f),
                                    size = Size(barW, h1),
                                    cornerRadius = CornerRadius(4f, 4f)
                                )
                                drawRoundRect(
                                    color = primaryColor,
                                    topLeft = Offset(w * 0.4f, (h - h2) / 2f),
                                    size = Size(barW, h2),
                                    cornerRadius = CornerRadius(4f, 4f)
                                )
                                drawRoundRect(
                                    color = primaryColor,
                                    topLeft = Offset(w * 0.7f, (h - h3) / 2f),
                                    size = Size(barW, h3),
                                    cornerRadius = CornerRadius(4f, 4f)
                                )
                            }
                            MediaPlayerState.PAUSED -> {
                                val barW = w * 0.24f
                                val barH = h * 0.68f
                                drawRoundRect(
                                    color = primaryColor,
                                    topLeft = Offset(w * 0.18f, (h - barH) / 2f),
                                    size = Size(barW, barH),
                                    cornerRadius = CornerRadius(4f, 4f)
                                )
                                drawRoundRect(
                                    color = primaryColor,
                                    topLeft = Offset(w * 0.58f, (h - barH) / 2f),
                                    size = Size(barW, barH),
                                    cornerRadius = CornerRadius(4f, 4f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* =======================================================================
   2. Child Transitions (createChildTransition)
   ======================================================================= */
enum class CardSelectionState {
    COLLAPSED_UNSELECTED,
    EXPANDED_SELECTED
}

enum class CheckmarkPopState {
    HIDDEN,
    POPPED
}

@OptIn(ExperimentalTransitionApi::class)
@Composable
private fun ChildTransitionCard() {
    var cardState by remember { mutableStateOf(CardSelectionState.COLLAPSED_UNSELECTED) }
    val speed = LocalAnimationSpeed.current
    val animDuration = scaledDuration(450, speed)
    val effectiveStiffness = (Spring.StiffnessMediumLow * speed * speed).coerceIn(30f, 50000f)

    val parentTransition = updateTransition(targetState = cardState, label = "ParentCardTransition")

    val cardBorderColor by parentTransition.animateColor(
        transitionSpec = { tween(animDuration) },
        label = "borderColor"
    ) { state ->
        when (state) {
            CardSelectionState.COLLAPSED_UNSELECTED -> ObsidianBorder
            CardSelectionState.EXPANDED_SELECTED -> NeonGreen
        }
    }

    val childTransition = parentTransition.createChildTransition(label = "CheckmarkPopChild") { parentState ->
        when (parentState) {
            CardSelectionState.COLLAPSED_UNSELECTED -> CheckmarkPopState.HIDDEN
            CardSelectionState.EXPANDED_SELECTED -> CheckmarkPopState.POPPED
        }
    }

    val checkmarkScale by childTransition.animateFloat(
        transitionSpec = {
            spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = effectiveStiffness)
        },
        label = "checkmarkScale"
    ) { checkState ->
        when (checkState) {
            CheckmarkPopState.HIDDEN -> 0.0f
            CheckmarkPopState.POPPED -> 1.1f
        }
    }

    val checkmarkRotation by childTransition.animateFloat(
        transitionSpec = {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = effectiveStiffness)
        },
        label = "checkmarkRotation"
    ) { checkState ->
        when (checkState) {
            CheckmarkPopState.HIDDEN -> -90f
            CheckmarkPopState.POPPED -> 0f
        }
    }

    MotionCard(
        title = "createChildTransition Hierarchy",
        apiTag = "Transition.createChildTransition",
        subtitle = "Decoupled child state machine inherits timing and lifecycle from parent Transition",
        accentColor = ElectricCyan
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(ObsidianBase)
                .border(1.5.dp, cardBorderColor, RoundedCornerShape(18.dp))
                .clickable {
                    cardState = if (cardState == CardSelectionState.COLLAPSED_UNSELECTED) {
                        CardSelectionState.EXPANDED_SELECTED
                    } else {
                        CardSelectionState.COLLAPSED_UNSELECTED
                    }
                }
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Enterprise Motion Engine Tier",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tap card to toggle parent & child transitions",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .scale(checkmarkScale)
                            .rotate(checkmarkRotation)
                            .clip(CircleShape)
                            .background(NeonGreen)
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = ObsidianBase,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                parentTransition.AnimatedVisibility(
                    visible = { it == CardSelectionState.EXPANDED_SELECTED },
                    enter = expandVertically(tween(animDuration)) + fadeIn(tween(animDuration)),
                    exit = shrinkVertically(tween(animDuration)) + fadeOut(tween(animDuration / 2))
                ) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ObsidianElevated)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "• Hardware-accelerated spring solvers\n• Shared element spatial interpolation\n• Deterministic child state propagation",
                                color = NeonGreen,
                                fontSize = 11.sp,
                                lineHeight = 17.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

/* =======================================================================
   3. transition.AnimatedVisibility & transition.AnimatedContent Extensions
   ======================================================================= */
@Composable
private fun TransitionExtensionsCard() {
    var isExpandedView by remember { mutableStateOf(true) }
    val speed = LocalAnimationSpeed.current
    val animDuration = scaledDuration(450, speed)

    val transition = updateTransition(targetState = isExpandedView, label = "ExtensionTransition")

    MotionCard(
        title = "Transition.Animated* Extensions",
        apiTag = "Transition.AnimatedVisibility / Content",
        subtitle = "Drive enter/exit layouts and content switches directly from a shared Transition instance",
        accentColor = CyberViolet
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ObsidianElevated)
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
                    .clickable { isExpandedView = !isExpandedView }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Master Transition Toggle",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Tap to toggle shared Transition state",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                transition.AnimatedContent(
                    transitionSpec = {
                        (scaleIn(tween(animDuration)) + fadeIn(tween(animDuration)))
                            .togetherWith(scaleOut(tween(animDuration)) + fadeOut(tween(animDuration)))
                    }
                ) { target ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (target) ElectricCyan else CyberViolet)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (target) "ACTIVE" else "STANDBY",
                            color = if (target) ObsidianBase else TextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            transition.AnimatedVisibility(
                visible = { it },
                enter = fadeIn(tween(animDuration)) + expandVertically(tween(animDuration)) +
                    slideInVertically(tween(animDuration)) { -it / 3 },
                exit = fadeOut(tween(animDuration / 2)) + shrinkVertically(tween(animDuration)) +
                    slideOutVertically(tween(animDuration)) { -it / 3 }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ObsidianBase)
                        .border(1.dp, ElectricCyan.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "Synchronized Sub-Panel",
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Both the status badge above (AnimatedContent) and this telemetry drawer (AnimatedVisibility) share the exact same Transition clock.",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
