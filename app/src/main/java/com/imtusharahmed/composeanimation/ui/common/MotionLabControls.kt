package com.imtusharahmed.composeanimation.ui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtusharahmed.composeanimation.ui.theme.CyberViolet
import com.imtusharahmed.composeanimation.ui.theme.ElectricCyan
import com.imtusharahmed.composeanimation.ui.theme.NeonGreen
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBase
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorder
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorderSubtle
import com.imtusharahmed.composeanimation.ui.theme.ObsidianCard
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.ObsidianSurface
import com.imtusharahmed.composeanimation.ui.theme.TextMuted
import com.imtusharahmed.composeanimation.ui.theme.TextPrimary
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary

val LocalAnimationSpeed = compositionLocalOf { 1.0f }

fun scaledDuration(baseMillis: Int, speedMultiplier: Float): Int =
    (baseMillis / speedMultiplier).toInt().coerceAtLeast(16)

enum class AnimationSpecType(val label: String) {
    SPRING("Spring"),
    TWEEN("Tween"),
    KEYFRAMES("Keyframes"),
    SNAP("Snap");

    fun <T> createSpec(speedMultiplier: Float, startVal: T, endVal: T): AnimationSpec<T> {
        val baseDuration = scaledDuration(600, speedMultiplier)
        return when (this) {
            SPRING -> spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = (Spring.StiffnessMedium * speedMultiplier * speedMultiplier).coerceIn(50f, 50000f)
            )
            TWEEN -> tween(
                durationMillis = baseDuration,
                easing = FastOutSlowInEasing
            )
            KEYFRAMES -> keyframes {
                durationMillis = baseDuration
                startVal at 0
                endVal at baseDuration
            }
            SNAP -> snap(delayMillis = scaledDuration(120, speedMultiplier))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MotionCard(
    title: String,
    apiTag: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    accentColor: Color = ElectricCyan,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        ObsidianCard,
                        ObsidianSurface
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        accentColor.copy(alpha = 0.45f),
                        ObsidianBorder,
                        CyberViolet.copy(alpha = 0.25f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(16.dp)
    ) {
        // Clean Header without horizontal cramping
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(accentColor, CyberViolet)
                            )
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Compact Live Indicator
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(NeonGreen.copy(alpha = 0.12f))
                    .border(1.dp, NeonGreen.copy(alpha = 0.35f), RoundedCornerShape(50))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(NeonGreen)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "LIVE",
                    color = NeonGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // API Badge Pill on its own clean row
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(accentColor.copy(alpha = 0.12f))
                .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = apiTag,
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Direct content without triple-nested box borders
        content()
    }
}

/**
 * Interactive animation stage with a subtle technical dot-grid background.
 */
@Composable
fun MotionStage(
    modifier: Modifier = Modifier,
    height: Dp = 175.dp,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianBase)
            .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(16.dp)),
        contentAlignment = contentAlignment
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val step = 22.dp.toPx()
            val dotRadius = 1.dp.toPx()
            val dotColor = ObsidianBorder.copy(alpha = 0.45f)
            var x = step
            while (x < size.width) {
                var y = step
                while (y < size.height) {
                    drawCircle(
                        color = dotColor,
                        radius = dotRadius,
                        center = Offset(x, y)
                    )
                    y += step
                }
                x += step
            }
        }
        content()
    }
}

/**
 * Scrollable or wrapped option pills so 3-5 options never squish or truncate.
 */
@Composable
fun <T> OptionPillStrip(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    labelProvider: (T) -> String,
    modifier: Modifier = Modifier,
    accentColor: Color = ElectricCyan
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            val bg by animateColorAsState(
                targetValue = if (isSelected) accentColor.copy(alpha = 0.2f) else ObsidianElevated,
                label = "pillBg"
            )
            val borderCol by animateColorAsState(
                targetValue = if (isSelected) accentColor else ObsidianBorder,
                label = "pillBorder"
            )
            val textCol by animateColorAsState(
                targetValue = if (isSelected) accentColor else TextSecondary,
                label = "pillText"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .border(1.dp, borderCol, RoundedCornerShape(10.dp))
                    .clickable { onOptionSelected(option) }
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = labelProvider(option),
                    color = textCol,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Tactile action button with press scale feedback.
 */
@Composable
fun LabActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = ElectricCyan,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "btnScale"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (enabled) 1f else 0.45f
            }
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.16f))
            .border(1.dp, color.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Clean telemetry strip for real-time metrics.
 */
@Composable
fun TelemetryStrip(
    items: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    accentColor: Color = ElectricCyan
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ObsidianElevated.copy(alpha = 0.75f))
            .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (label, value) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$label: ",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = value,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun SpeedMultiplierBar(
    selectedSpeed: Float,
    onSpeedSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val speeds = listOf(0.2f to "0.2x Slow", 0.5f to "0.5x Half", 1.0f to "1.0x Normal", 2.0f to "2.0x Fast")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        speeds.forEach { (speed, label) ->
            val isSelected = selectedSpeed == speed
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) ElectricCyan else Color.Transparent,
                label = "speedBg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) ObsidianBase else TextSecondary,
                label = "speedText"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor)
                    .clickable { onSpeedSelected(speed) }
                    .padding(vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = textColor,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun AnimationSpecSelector(
    selectedSpec: AnimationSpecType,
    onSpecSelected: (AnimationSpecType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianElevated)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AnimationSpecType.entries.forEach { spec ->
            val isSelected = selectedSpec == spec
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) CyberViolet else Color.Transparent,
                label = "specBg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) TextPrimary else TextSecondary,
                label = "specText"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(9.dp))
                    .background(bgColor)
                    .clickable { onSpecSelected(spec) }
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = spec.label,
                    color = textColor,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}
