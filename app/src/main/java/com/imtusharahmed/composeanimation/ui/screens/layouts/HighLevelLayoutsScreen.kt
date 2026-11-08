package com.imtusharahmed.composeanimation.ui.screens.layouts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.imtusharahmed.composeanimation.ui.common.scaledDuration
import com.imtusharahmed.composeanimation.ui.theme.AmberGlow
import com.imtusharahmed.composeanimation.ui.theme.CyberViolet
import com.imtusharahmed.composeanimation.ui.theme.ElectricBlue
import com.imtusharahmed.composeanimation.ui.theme.ElectricCyan
import com.imtusharahmed.composeanimation.ui.theme.NeonGreen
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBase
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorder
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorderSubtle
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.ObsidianSurface
import com.imtusharahmed.composeanimation.ui.theme.RadiantSunset
import com.imtusharahmed.composeanimation.ui.theme.TextMuted
import com.imtusharahmed.composeanimation.ui.theme.TextPrimary
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun HighLevelLayoutsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AnimatedVisibilityCard()
        AnimatedContentCard()
        CrossfadeCard()
        AnimateContentSizeCard()
    }
}

/* =======================================================================
   1. AnimatedVisibility: All Enter/Exit & Nested animateEnterExit
   ======================================================================= */
enum class TransitionCombo(val label: String) {
    NESTED_STAGGER("Nested Cascade"),
    FADE_SCALE("Fade + Scale"),
    VERTICAL_SLIDE("Vertical Slide"),
    HORIZONTAL_SLIDE("Horizontal Slide"),
    FULL_CUSTOM("2D Diagonal")
}

@Composable
private fun AnimatedVisibilityCard() {
    var isVisible by remember { mutableStateOf(true) }
    var selectedCombo by remember { mutableStateOf(TransitionCombo.NESTED_STAGGER) }
    var selectedSpec by remember { mutableStateOf(AnimationSpecType.SPRING) }
    val speed = LocalAnimationSpeed.current

    val animDuration = scaledDuration(550, speed)
    val effectiveStiffness = (Spring.StiffnessMediumLow * speed * speed).coerceIn(40f, 50000f)

    val floatSpec: FiniteAnimationSpec<Float> = remember(selectedSpec, speed) {
        when (selectedSpec) {
            AnimationSpecType.SPRING -> spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = effectiveStiffness
            )
            AnimationSpecType.TWEEN -> tween(durationMillis = animDuration, easing = FastOutSlowInEasing)
            AnimationSpecType.KEYFRAMES -> keyframes {
                durationMillis = animDuration
                0.4f at (animDuration * 0.35f).toInt()
                1.08f at (animDuration * 0.75f).toInt()
            }
            AnimationSpecType.SNAP -> snap(delayMillis = scaledDuration(80, speed))
        }
    }

    val offsetSpec: FiniteAnimationSpec<IntOffset> = remember(selectedSpec, speed) {
        when (selectedSpec) {
            AnimationSpecType.SPRING -> spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = effectiveStiffness
            )
            AnimationSpecType.TWEEN -> tween(durationMillis = animDuration, easing = FastOutSlowInEasing)
            AnimationSpecType.KEYFRAMES -> tween(durationMillis = animDuration, easing = LinearOutSlowInEasing)
            AnimationSpecType.SNAP -> snap(delayMillis = scaledDuration(80, speed))
        }
    }

    val sizeSpec: FiniteAnimationSpec<IntSize> = remember(selectedSpec, speed) {
        when (selectedSpec) {
            AnimationSpecType.SPRING -> spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = effectiveStiffness
            )
            AnimationSpecType.TWEEN -> tween(durationMillis = animDuration, easing = FastOutSlowInEasing)
            AnimationSpecType.KEYFRAMES -> tween(durationMillis = animDuration, easing = LinearOutSlowInEasing)
            AnimationSpecType.SNAP -> snap(delayMillis = scaledDuration(80, speed))
        }
    }

    val enterTransition = remember(selectedCombo, floatSpec, offsetSpec, sizeSpec) {
        when (selectedCombo) {
            TransitionCombo.FADE_SCALE ->
                fadeIn(animationSpec = floatSpec) +
                    scaleIn(animationSpec = floatSpec, initialScale = 0.45f)

            TransitionCombo.VERTICAL_SLIDE ->
                slideInVertically(
                    animationSpec = offsetSpec,
                    initialOffsetY = { -it }
                ) + expandVertically(animationSpec = sizeSpec, expandFrom = Alignment.Top) +
                    fadeIn(animationSpec = floatSpec)

            TransitionCombo.HORIZONTAL_SLIDE ->
                slideInHorizontally(
                    animationSpec = offsetSpec,
                    initialOffsetX = { -it }
                ) + expandHorizontally(animationSpec = sizeSpec, expandFrom = Alignment.Start) +
                    fadeIn(animationSpec = floatSpec)

            TransitionCombo.FULL_CUSTOM ->
                fadeIn(animationSpec = floatSpec) +
                    slideIn(animationSpec = offsetSpec, initialOffset = { IntOffset(-it.width / 2, -it.height / 2) }) +
                    scaleIn(animationSpec = floatSpec, initialScale = 0.35f) +
                    expandIn(animationSpec = sizeSpec, expandFrom = Alignment.Center)

            TransitionCombo.NESTED_STAGGER ->
                fadeIn(animationSpec = tween(animDuration / 2)) +
                    expandVertically(animationSpec = sizeSpec, expandFrom = Alignment.Top)
        }
    }

    val exitTransition = remember(selectedCombo, floatSpec, offsetSpec, sizeSpec) {
        when (selectedCombo) {
            TransitionCombo.FADE_SCALE ->
                fadeOut(animationSpec = floatSpec) +
                    scaleOut(animationSpec = floatSpec, targetScale = 0.45f)

            TransitionCombo.VERTICAL_SLIDE ->
                slideOutVertically(
                    animationSpec = offsetSpec,
                    targetOffsetY = { it }
                ) + shrinkVertically(animationSpec = sizeSpec, shrinkTowards = Alignment.Bottom) +
                    fadeOut(animationSpec = floatSpec)

            TransitionCombo.HORIZONTAL_SLIDE ->
                slideOutHorizontally(
                    animationSpec = offsetSpec,
                    targetOffsetX = { it }
                ) + shrinkHorizontally(animationSpec = sizeSpec, shrinkTowards = Alignment.End) +
                    fadeOut(animationSpec = floatSpec)

            TransitionCombo.FULL_CUSTOM ->
                fadeOut(animationSpec = floatSpec) +
                    slideOut(animationSpec = offsetSpec, targetOffset = { IntOffset(it.width / 2, it.height / 2) }) +
                    scaleOut(animationSpec = floatSpec, targetScale = 0.35f) +
                    shrinkOut(animationSpec = sizeSpec, shrinkTowards = Alignment.Center)

            TransitionCombo.NESTED_STAGGER ->
                fadeOut(animationSpec = tween(animDuration)) +
                    shrinkVertically(animationSpec = sizeSpec, shrinkTowards = Alignment.Top)
        }
    }

    MotionCard(
        title = "AnimatedVisibility Matrix",
        apiTag = "AnimatedVisibility + animateEnterExit",
        subtitle = "Combine 8 enter/exit transitions with live spec switching & staggered child cascades",
        accentColor = ElectricCyan
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OptionPillStrip(
                options = TransitionCombo.entries,
                selectedOption = selectedCombo,
                onOptionSelected = { selectedCombo = it },
                labelProvider = { it.label },
                accentColor = ElectricCyan
            )

            AnimationSpecSelector(
                selectedSpec = selectedSpec,
                onSpecSelected = { selectedSpec = it }
            )

            MotionStage(height = 195.dp) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isVisible,
                    enter = enterTransition,
                    exit = exitTransition
                ) {
                    if (selectedCombo == TransitionCombo.NESTED_STAGGER) {
                        NestedStaggerContent(scope = this, speed = speed)
                    } else {
                        StandardPreviewCard(
                            title = selectedCombo.label,
                            specLabel = selectedSpec.label
                        )
                    }
                }
            }

            LabActionButton(
                text = if (isVisible) "Trigger Exit Transition (Hide)" else "Trigger Enter Transition (Show)",
                onClick = { isVisible = !isVisible },
                modifier = Modifier.fillMaxWidth(),
                color = if (isVisible) RadiantSunset else ElectricCyan
            )
        }
    }
}

@Composable
private fun NestedStaggerContent(
    scope: AnimatedVisibilityScope,
    speed: Float
) {
    val d1 = scaledDuration(380, speed)
    val stagger = scaledDuration(110, speed)

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        with(scope) {
            CascadeNode(
                index = "01",
                label = "slideInHorizontally (0ms delay)",
                color = ElectricCyan,
                modifier = Modifier.animateEnterExit(
                    enter = slideInHorizontally(
                        animationSpec = tween(d1, delayMillis = 0, easing = FastOutSlowInEasing),
                        initialOffsetX = { -it / 2 }
                    ) + fadeIn(tween(d1, delayMillis = 0)),
                    exit = slideOutHorizontally(
                        animationSpec = tween(d1 / 2, delayMillis = stagger * 2),
                        targetOffsetX = { it / 2 }
                    ) + fadeOut(tween(d1 / 2, delayMillis = stagger * 2))
                )
            )

            CascadeNode(
                index = "02",
                label = "scaleIn + fadeIn (+${stagger}ms delay)",
                color = CyberViolet,
                modifier = Modifier.animateEnterExit(
                    enter = scaleIn(
                        animationSpec = tween(d1, delayMillis = stagger, easing = FastOutSlowInEasing),
                        initialScale = 0.5f
                    ) + fadeIn(tween(d1, delayMillis = stagger)),
                    exit = scaleOut(
                        animationSpec = tween(d1 / 2, delayMillis = stagger),
                        targetScale = 0.5f
                    ) + fadeOut(tween(d1 / 2, delayMillis = stagger))
                )
            )

            CascadeNode(
                index = "03",
                label = "slideInVertically (+${stagger * 2}ms delay)",
                color = AmberGlow,
                modifier = Modifier.animateEnterExit(
                    enter = slideInVertically(
                        animationSpec = tween(d1, delayMillis = stagger * 2, easing = FastOutSlowInEasing),
                        initialOffsetY = { it }
                    ) + fadeIn(tween(d1, delayMillis = stagger * 2)),
                    exit = slideOutVertically(
                        animationSpec = tween(d1 / 2, delayMillis = 0),
                        targetOffsetY = { it }
                    ) + fadeOut(tween(d1 / 2, delayMillis = 0))
                )
            )
        }
    }
}

@Composable
private fun CascadeNode(
    index: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianElevated)
            .border(1.dp, color.copy(alpha = 0.55f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(color.copy(alpha = 0.2f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = index,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun StandardPreviewCard(title: String, specLabel: String) {
    Box(
        modifier = Modifier
            .size(width = 230.dp, height = 115.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(ObsidianElevated, CyberViolet.copy(alpha = 0.35f), ObsidianSurface)
                )
            )
            .border(1.5.dp, ElectricCyan, RoundedCornerShape(18.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = ElectricCyan,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ObsidianBase.copy(alpha = 0.6f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "Spec: $specLabel",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/* =======================================================================
   2. AnimatedContent: Stock Price Ticker & Checkout Progress Flow
   ======================================================================= */
@Composable
private fun AnimatedContentCard() {
    var stockPrice by remember { mutableDoubleStateOf(184.25) }
    var previousPrice by remember { mutableDoubleStateOf(180.00) }
    var checkoutStep by remember { mutableIntStateOf(1) }
    val speed = LocalAnimationSpeed.current
    val animDuration = scaledDuration(450, speed)

    MotionCard(
        title = "AnimatedContent Choreography",
        apiTag = "AnimatedContent + SizeTransform",
        subtitle = "Directional numeric counter roll & multi-step wizard with animated progress",
        accentColor = CyberViolet
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Part 1: Stock Ticker
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ObsidianBase)
                    .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "NASDAQ: CMPX",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val isUp = stockPrice >= previousPrice
                        Text(
                            text = if (isUp) "▲ BULL" else "▼ BEAR",
                            color = if (isUp) NeonGreen else RadiantSunset,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    AnimatedContent(
                        targetState = stockPrice,
                        transitionSpec = {
                            if (targetState >= initialState) {
                                (slideInVertically(animationSpec = tween(animDuration)) { it } +
                                    fadeIn(animationSpec = tween(animDuration)))
                                    .togetherWith(
                                        slideOutVertically(animationSpec = tween(animDuration)) { -it } +
                                            fadeOut(animationSpec = tween(animDuration))
                                    )
                            } else {
                                (slideInVertically(animationSpec = tween(animDuration)) { -it } +
                                    fadeIn(animationSpec = tween(animDuration)))
                                    .togetherWith(
                                        slideOutVertically(animationSpec = tween(animDuration)) { it } +
                                            fadeOut(animationSpec = tween(animDuration))
                                    )
                            }.using(SizeTransform(clip = false))
                        },
                        label = "stockTicker"
                    ) { price ->
                        val isUp = price >= previousPrice
                        Text(
                            text = String.format(Locale.US, "$%.2f", price),
                            color = if (isUp) NeonGreen else RadiantSunset,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LabActionButton(
                        text = "+$5.75",
                        onClick = {
                            previousPrice = stockPrice
                            stockPrice += 5.75
                        },
                        color = NeonGreen
                    )
                    LabActionButton(
                        text = "-$4.50",
                        onClick = {
                            previousPrice = stockPrice
                            stockPrice = (stockPrice - 4.50).coerceAtLeast(10.0)
                        },
                        color = RadiantSunset
                    )
                }
            }

            // Part 2: Stepper Wizard
            val stepLabels = listOf("Cart", "Shipping", "Payment", "Done")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                stepLabels.forEachIndexed { index, label ->
                    val stepNum = index + 1
                    val isCompleted = checkoutStep > stepNum
                    val isCurrent = checkoutStep == stepNum
                    val nodeColor by animateColorAsState(
                        targetValue = when {
                            isCurrent -> ElectricCyan
                            isCompleted -> NeonGreen
                            else -> ObsidianElevated
                        },
                        label = "stepNodeColor"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(nodeColor)
                                .border(
                                    1.dp,
                                    if (isCurrent || isCompleted) Color.Transparent else ObsidianBorder,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isCompleted) "✓" else "$stepNum",
                                color = if (isCurrent || isCompleted) ObsidianBase else TextSecondary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = label,
                            color = if (isCurrent) ElectricCyan else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                        )
                    }

                    if (index < stepLabels.lastIndex) {
                        val lineProgress by animateFloatAsState(
                            targetValue = if (checkoutStep > stepNum) 1f else 0f,
                            animationSpec = tween(animDuration),
                            label = "connectorLine"
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 6.dp)
                                .padding(bottom = 16.dp)
                                .height(2.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(ObsidianElevated)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(lineProgress)
                                    .height(2.dp)
                                    .background(NeonGreen)
                            )
                        }
                    }
                }
            }

            AnimatedContent(
                targetState = checkoutStep,
                transitionSpec = {
                    val forward = targetState > initialState
                    (slideInHorizontally(animationSpec = tween(animDuration)) { if (forward) it else -it } +
                        fadeIn(tween(animDuration)))
                        .togetherWith(
                            slideOutHorizontally(animationSpec = tween(animDuration)) { if (forward) -it else it } +
                                fadeOut(tween(animDuration))
                        )
                        .using(SizeTransform(clip = false))
                },
                label = "checkoutContent"
            ) { step ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ObsidianBase)
                        .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    when (step) {
                        1 -> StepDetailContent(
                            tag = "STEP 01 • CART",
                            title = "Jetpack Compose Motion Kit (3 Items)",
                            subtitle = "Physics Springs, Shared Transitions & Shader Pack • $199.00",
                            accent = ElectricCyan
                        )
                        2 -> StepDetailContent(
                            tag = "STEP 02 • DISPATCH",
                            title = "77 Silicon Boulevard, Android Metropolis",
                            subtitle = "Instant OTA Cloud Sync Delivery Enabled",
                            accent = CyberViolet
                        )
                        3 -> StepDetailContent(
                            tag = "STEP 03 • ENCRYPTED PAY",
                            title = "Zero-Knowledge Biometric Vault",
                            subtitle = "Hardware-backed token verification ready",
                            accent = AmberGlow
                        )
                        else -> StepDetailContent(
                            tag = "STEP 04 • COMPLETE",
                            title = "Order #77092 Provisioned!",
                            subtitle = "All motion engines unlocked in your workspace.",
                            accent = NeonGreen
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LabActionButton(
                    text = "← Previous",
                    onClick = { if (checkoutStep > 1) checkoutStep-- },
                    modifier = Modifier.weight(1f),
                    color = TextSecondary,
                    enabled = checkoutStep > 1
                )
                LabActionButton(
                    text = if (checkoutStep == 4) "Restart Flow ↺" else "Next Step →",
                    onClick = { if (checkoutStep < 4) checkoutStep++ else checkoutStep = 1 },
                    modifier = Modifier.weight(1f),
                    color = ElectricCyan
                )
            }
        }
    }
}

@Composable
private fun StepDetailContent(
    tag: String,
    title: String,
    subtitle: String,
    accent: Color
) {
    Column {
        Text(
            text = tag,
            color = accent,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

/* =======================================================================
   3. Crossfade: LoadingShimmer, EmptyState, DataContent
   ======================================================================= */
enum class CrossfadeState(val label: String) {
    DATA_CONTENT("Live Telemetry"),
    LOADING_SHIMMER("Shimmer Skeleton"),
    EMPTY_STATE("Empty State")
}

@Composable
private fun CrossfadeCard() {
    var currentState by remember { mutableStateOf(CrossfadeState.DATA_CONTENT) }
    var useSpring by remember { mutableStateOf(false) }
    val speed = LocalAnimationSpeed.current

    val animDuration = scaledDuration(600, speed)
    val animationSpec: FiniteAnimationSpec<Float> = remember(useSpring, speed) {
        if (useSpring) {
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = (Spring.StiffnessLow * speed * speed).coerceIn(40f, 50000f)
            )
        } else {
            tween(durationMillis = animDuration, easing = LinearOutSlowInEasing)
        }
    }

    MotionCard(
        title = "Crossfade State Dissolve",
        apiTag = "Crossfade",
        subtitle = "Smoothly cross-dissolve between Live Telemetry, Animated Skeleton, and Empty State",
        accentColor = ElectricBlue
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OptionPillStrip(
                    options = CrossfadeState.entries,
                    selectedOption = currentState,
                    onOptionSelected = { currentState = it },
                    labelProvider = { it.label },
                    accentColor = ElectricBlue,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(ObsidianElevated)
                        .border(1.dp, ElectricCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .clickable { useSpring = !useSpring }
                        .padding(horizontal = 10.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = if (useSpring) "Spring" else "Tween",
                        color = ElectricCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            MotionStage(height = 140.dp) {
                Crossfade(
                    targetState = currentState,
                    animationSpec = animationSpec,
                    label = "crossfadeContent"
                ) { state ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (state) {
                            CrossfadeState.LOADING_SHIMMER -> {
                                val infinite = rememberInfiniteTransition(label = "crossfadeShimmer")
                                val shimmerX by infinite.animateFloat(
                                    initialValue = -300f,
                                    targetValue = 700f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(scaledDuration(1200, speed), easing = LinearEasing),
                                        repeatMode = RepeatMode.Restart
                                    ),
                                    label = "shimmerX"
                                )
                                val brush = Brush.linearGradient(
                                    colors = listOf(
                                        ObsidianElevated,
                                        ElectricBlue.copy(alpha = 0.35f),
                                        ObsidianElevated
                                    ),
                                    start = Offset(shimmerX, 0f),
                                    end = Offset(shimmerX + 260f, 120f)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(brush)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.75f)
                                                .height(14.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(brush)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.95f)
                                                .height(10.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(brush)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.45f)
                                                .height(10.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(brush)
                                        )
                                    }
                                }
                            }

                            CrossfadeState.EMPTY_STATE -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(ObsidianElevated)
                                            .border(1.dp, ObsidianBorder, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("∅", color = AmberGlow, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "No Telemetry Streams Found",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Switch back to Live Telemetry to inspect GPU nodes",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            CrossfadeState.DATA_CONTENT -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "NVIDIA H200 TENSOR CORE",
                                            color = ElectricCyan,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Clock: 2,450 MHz • Frame Time: 4.1 ms",
                                            color = TextPrimary,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = "Thermals: 54°C • VRAM: 118 / 141 GB",
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(NeonGreen.copy(alpha = 0.16f))
                                            .border(1.dp, NeonGreen, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "99.8% SYNC",
                                            color = NeonGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/* =======================================================================
   4. Modifier.animateContentSize: Accordion & Dynamic Tag Pills
   ======================================================================= */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnimateContentSizeCard() {
    var isExpanded by remember { mutableStateOf(false) }
    val tags = remember { mutableStateListOf("Compose", "Springs", "Lookahead", "Shaders") }
    var selectedAlignment by remember { mutableStateOf(Alignment.TopStart) }
    val speed = LocalAnimationSpeed.current

    val alignments = remember {
        listOf(
            Alignment.TopStart to "TopStart",
            Alignment.Center to "Center",
            Alignment.BottomEnd to "BottomEnd"
        )
    }

    MotionCard(
        title = "animateContentSize Morph",
        apiTag = "Modifier.animateContentSize",
        subtitle = "Automatic layout bounds interpolation for accordions & dynamic tag clouds",
        accentColor = NeonGreen
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OptionPillStrip(
                options = alignments,
                selectedOption = alignments.first { it.first == selectedAlignment },
                onOptionSelected = { selectedAlignment = it.first },
                labelProvider = { "Align: ${it.second}" },
                accentColor = NeonGreen
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianBase)
                    .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(16.dp))
                    .padding(14.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = (Spring.StiffnessMediumLow * speed * speed).coerceIn(40f, 50000f)
                        ),
                        alignment = selectedAlignment
                    )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isExpanded = !isExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Declarative Frame-Clock Architecture",
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (isExpanded) "▲ Collapse" else "▼ Expand",
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isExpanded) {
                            "Compose's animation engine measures target layout bounds before drawing and interpolates width and height across every frame. Combined with spring physics, layout changes never jump or snap abruptly—even if you toggle mid-animation."
                        } else {
                            "Compose's animation engine measures target layout bounds before drawing..."
                        },
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ObsidianElevated)
                                    .border(1.dp, ObsidianBorder, RoundedCornerShape(8.dp))
                                    .clickable { if (tags.size > 1) tags.remove(tag) }
                                    .padding(horizontal = 9.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "#$tag ×",
                                    color = ElectricBlue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            val candidateTags = listOf("Gestures", "Coroutines", "Anchors", "Keyframes", "Decay", "Vectors", "Bounds")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LabActionButton(
                    text = "+ Add Tag Pill",
                    onClick = {
                        val available = candidateTags.filter { !tags.contains(it) }
                        if (available.isNotEmpty()) {
                            tags.add(available.random())
                        }
                    },
                    modifier = Modifier.weight(1f),
                    color = NeonGreen,
                    enabled = tags.size < candidateTags.size + 4
                )
                LabActionButton(
                    text = "- Remove Tag",
                    onClick = {
                        if (tags.isNotEmpty()) {
                            tags.removeAt(tags.lastIndex)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    color = RadiantSunset,
                    enabled = tags.isNotEmpty()
                )
            }
        }
    }
}
