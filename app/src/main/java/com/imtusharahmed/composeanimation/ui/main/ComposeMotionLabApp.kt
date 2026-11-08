package com.imtusharahmed.composeanimation.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtusharahmed.composeanimation.ui.common.LocalAnimationSpeed
import com.imtusharahmed.composeanimation.ui.common.SpeedMultiplierBar
import com.imtusharahmed.composeanimation.ui.screens.infinite.InfiniteAnimationsScreen
import com.imtusharahmed.composeanimation.ui.screens.layouts.HighLevelLayoutsScreen
import com.imtusharahmed.composeanimation.ui.screens.lowlevel.LowLevelEnginesScreen
import com.imtusharahmed.composeanimation.ui.screens.physics.GesturesPhysicsScreen
import com.imtusharahmed.composeanimation.ui.screens.shared.LookaheadAndSharedScreen
import com.imtusharahmed.composeanimation.ui.screens.specs.AnimationSpecGalleryScreen
import com.imtusharahmed.composeanimation.ui.screens.transitions.StateMachinesScreen
import com.imtusharahmed.composeanimation.ui.screens.values.ValueAnimationsScreen
import com.imtusharahmed.composeanimation.ui.theme.AmberGlow
import com.imtusharahmed.composeanimation.ui.theme.CyberPink
import com.imtusharahmed.composeanimation.ui.theme.CyberViolet
import com.imtusharahmed.composeanimation.ui.theme.ElectricBlue
import com.imtusharahmed.composeanimation.ui.theme.ElectricCyan
import com.imtusharahmed.composeanimation.ui.theme.NeonGreen
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBase
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorder
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorderSubtle
import com.imtusharahmed.composeanimation.ui.theme.ObsidianCard
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.ObsidianSurface
import com.imtusharahmed.composeanimation.ui.theme.RadiantSunset
import com.imtusharahmed.composeanimation.ui.theme.TextMuted
import com.imtusharahmed.composeanimation.ui.theme.TextPrimary
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary

enum class MotionLabSection(
    val title: String,
    val badge: String,
    val description: String,
    val accentColor: Color
) {
    LAYOUTS(
        title = "Visibility & Morph",
        badge = "4 Demos",
        description = "AnimatedVisibility, AnimatedContent, Crossfade & animateContentSize",
        accentColor = ElectricCyan
    ),
    SHARED(
        title = "Shared & Lookahead",
        badge = "2 Demos",
        description = "SharedTransitionLayout master-detail & LookaheadScope choreography",
        accentColor = ElectricBlue
    ),
    VALUES(
        title = "State Values",
        badge = "4 Demos",
        description = "animate*AsState suite & custom 4D TwoWayConverter",
        accentColor = CyberViolet
    ),
    SPECS(
        title = "Spec Gallery",
        badge = "3 Demos",
        description = "Spring damping ratios, Tween easing curves, Keyframes & Snap",
        accentColor = AmberGlow
    ),
    TRANSITIONS(
        title = "State Machines",
        badge = "3 Demos",
        description = "updateTransition, createChildTransition & Transition extensions",
        accentColor = NeonGreen
    ),
    INFINITE(
        title = "Infinite Loops",
        badge = "3 Demos",
        description = "Radar pulse beacon, Shimmer sweep & breathing neon core",
        accentColor = ElectricCyan
    ),
    PHYSICS(
        title = "Touch Gestures",
        badge = "2 Demos",
        description = "Velocity-tracked swipe dismiss & multi-anchor draggable sheet",
        accentColor = RadiantSunset
    ),
    LOW_LEVEL(
        title = "Coroutines & Clock",
        badge = "3 Demos",
        description = "Imperative Animatable, withFrameNanos loop & DecayAnimation",
        accentColor = CyberPink
    )
}

enum class MotionNavCategory(
    val label: String,
    val subtitle: String,
    val accentColor: Color,
    val sections: List<MotionLabSection>
) {
    LAYOUTS(
        label = "Layouts",
        subtitle = "High-Level & Shared",
        accentColor = ElectricCyan,
        sections = listOf(MotionLabSection.LAYOUTS, MotionLabSection.SHARED)
    ),
    VALUES(
        label = "Values",
        subtitle = "States & Curve Specs",
        accentColor = CyberViolet,
        sections = listOf(MotionLabSection.VALUES, MotionLabSection.SPECS)
    ),
    TRANSITIONS(
        label = "Machines",
        subtitle = "Transitions & Loops",
        accentColor = NeonGreen,
        sections = listOf(MotionLabSection.TRANSITIONS, MotionLabSection.INFINITE)
    ),
    PHYSICS(
        label = "Physics",
        subtitle = "Gestures & Engines",
        accentColor = RadiantSunset,
        sections = listOf(MotionLabSection.PHYSICS, MotionLabSection.LOW_LEVEL)
    )
}

@Composable
fun ComposeMotionLabApp() {
    var currentCategory by remember { mutableStateOf(MotionNavCategory.LAYOUTS) }
    var currentSection by remember { mutableStateOf(MotionLabSection.LAYOUTS) }
    var speedMultiplier by remember { mutableFloatStateOf(1.0f) }
    var showSpeedPanel by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalAnimationSpeed provides speedMultiplier) {
        Scaffold(
            containerColor = ObsidianBase,
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                MotionBottomNavigationBar(
                    currentCategory = currentCategory,
                    onCategorySelected = { category ->
                        if (currentCategory != category) {
                            currentCategory = category
                            currentSection = category.sections.first()
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .statusBarsPadding()
            ) {
                // Sleek Top App Bar with Collapsible Time-Scale Trigger
                MotionLabTopAppBar(
                    currentCategory = currentCategory,
                    speedMultiplier = speedMultiplier,
                    isSpeedExpanded = showSpeedPanel,
                    onToggleSpeedPanel = { showSpeedPanel = !showSpeedPanel }
                )

                // Collapsible Global Time Scale Bar
                AnimatedVisibility(
                    visible = showSpeedPanel,
                    enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(ObsidianCard)
                            .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "GLOBAL ANIMATION TIME SCALE",
                                color = TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Active: ${speedMultiplier}x",
                                color = ElectricCyan,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        SpeedMultiplierBar(
                            selectedSpeed = speedMultiplier,
                            onSpeedSelected = { speedMultiplier = it }
                        )
                    }
                }

                // Segmented Sub-Section Switcher for Current BottomNav Category
                SubSectionSegmentedBar(
                    sections = currentCategory.sections,
                    currentSection = currentSection,
                    onSectionSelected = { currentSection = it }
                )

                // Section Info Banner
                SectionHeaderBanner(section = currentSection)

                // Directional Animated Content Area with independent ScrollState per section
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    AnimatedContent(
                        targetState = currentSection,
                        transitionSpec = {
                            val direction = if (targetState.ordinal >= initialState.ordinal) 1 else -1
                            (slideInHorizontally(
                                animationSpec = tween(280, easing = FastOutSlowInEasing),
                                initialOffsetX = { fullWidth -> direction * (fullWidth / 4) }
                            ) + fadeIn(tween(240))) togetherWith
                                (slideOutHorizontally(
                                    animationSpec = tween(220, easing = FastOutSlowInEasing),
                                    targetOffsetX = { fullWidth -> -direction * (fullWidth / 4) }
                                ) + fadeOut(tween(180)))
                        },
                        label = "sectionContentSwitcher"
                    ) { section ->
                        val sectionScrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(sectionScrollState)
                                .padding(horizontal = 16.dp)
                                .padding(top = 4.dp, bottom = 28.dp)
                        ) {
                            when (section) {
                                MotionLabSection.LAYOUTS -> HighLevelLayoutsScreen()
                                MotionLabSection.SHARED -> LookaheadAndSharedScreen()
                                MotionLabSection.VALUES -> ValueAnimationsScreen()
                                MotionLabSection.SPECS -> AnimationSpecGalleryScreen()
                                MotionLabSection.TRANSITIONS -> StateMachinesScreen()
                                MotionLabSection.INFINITE -> InfiniteAnimationsScreen()
                                MotionLabSection.PHYSICS -> GesturesPhysicsScreen()
                                MotionLabSection.LOW_LEVEL -> LowLevelEnginesScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MotionLabTopAppBar(
    currentCategory: MotionNavCategory,
    speedMultiplier: Float,
    isSpeedExpanded: Boolean,
    onToggleSpeedPanel: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logoPulse")
    val orbPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbScale"
    )
    val accentColor by animateColorAsState(
        targetValue = currentCategory.accentColor,
        animationSpec = tween(350),
        label = "topBarAccent"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ObsidianElevated)
                    .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .graphicsLayer {
                            scaleX = orbPulse
                            scaleY = orbPulse
                        }
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.3f))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "COMPOSE MOTION LAB",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "${currentCategory.label} • ${currentCategory.subtitle}",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Interactive Time-Scale Chip
        val chipBg by animateColorAsState(
            targetValue = if (isSpeedExpanded || speedMultiplier != 1.0f) {
                ElectricCyan.copy(alpha = 0.18f)
            } else {
                ObsidianElevated
            },
            label = "speedChipBg"
        )
        val chipBorder by animateColorAsState(
            targetValue = if (isSpeedExpanded || speedMultiplier != 1.0f) {
                ElectricCyan
            } else {
                ObsidianBorder
            },
            label = "speedChipBorder"
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(chipBg)
                .border(1.dp, chipBorder, RoundedCornerShape(12.dp))
                .clickable { onToggleSpeedPanel() }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(if (speedMultiplier != 1.0f) AmberGlow else ElectricCyan)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "${speedMultiplier}x SPEED",
                color = if (isSpeedExpanded || speedMultiplier != 1.0f) ElectricCyan else TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
private fun SubSectionSegmentedBar(
    sections: List<MotionLabSection>,
    currentSection: MotionLabSection,
    onSectionSelected: (MotionLabSection) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianBorderSubtle, RoundedCornerShape(14.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        sections.forEach { section ->
            val isSelected = section == currentSection
            val bg by animateColorAsState(
                targetValue = if (isSelected) ObsidianElevated else Color.Transparent,
                animationSpec = tween(220),
                label = "subTabBg"
            )
            val borderCol by animateColorAsState(
                targetValue = if (isSelected) section.accentColor.copy(alpha = 0.7f) else Color.Transparent,
                animationSpec = tween(220),
                label = "subTabBorder"
            )
            val titleColor by animateColorAsState(
                targetValue = if (isSelected) TextPrimary else TextSecondary,
                animationSpec = tween(220),
                label = "subTabText"
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .border(1.dp, borderCol, RoundedCornerShape(10.dp))
                    .clickable { onSectionSelected(section) }
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) section.accentColor else TextMuted)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = section.title,
                        color = titleColor,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isSelected) section.accentColor.copy(alpha = 0.16f)
                            else ObsidianBase.copy(alpha = 0.5f)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = section.badge,
                        color = if (isSelected) section.accentColor else TextMuted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeaderBanner(section: MotionLabSection) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = section.description,
            color = TextSecondary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MotionBottomNavigationBar(
    currentCategory: MotionNavCategory,
    onCategorySelected: (MotionNavCategory) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        ObsidianBase.copy(alpha = 0.0f),
                        ObsidianBase.copy(alpha = 0.92f),
                        ObsidianBase
                    )
                )
            )
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(ObsidianCard)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            ObsidianBorder,
                            currentCategory.accentColor.copy(alpha = 0.45f),
                            ObsidianBorder
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MotionNavCategory.entries.forEach { category ->
                val isSelected = category == currentCategory
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.05f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "navScale"
                )
                val pillWidth by animateDpAsState(
                    targetValue = if (isSelected) 54.dp else 0.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "navPillWidth"
                )
                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) category.accentColor else TextMuted,
                    animationSpec = tween(250),
                    label = "navIconColor"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) TextPrimary else TextSecondary,
                    animationSpec = tween(250),
                    label = "navTextColor"
                )

                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onCategorySelected(category) }
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.height(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pillWidth > 1.dp) {
                            Box(
                                modifier = Modifier
                                    .width(pillWidth)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(category.accentColor.copy(alpha = 0.16f))
                                    .border(
                                        1.dp,
                                        category.accentColor.copy(alpha = 0.4f),
                                        RoundedCornerShape(14.dp)
                                    )
                            )
                        }
                        CategoryVectorIcon(
                            category = category,
                            color = iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = category.label,
                        color = textColor,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryVectorIcon(
    category: MotionNavCategory,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        val w = size.width
        val h = size.height

        when (category) {
            MotionNavCategory.LAYOUTS -> {
                // Bento Grid Layout Icon
                val gap = w * 0.1f
                val half = (w - gap) / 2f
                drawRoundRect(
                    color = color,
                    topLeft = Offset(0f, 0f),
                    size = Size(half, half),
                    cornerRadius = CornerRadius(3.dp.toPx()),
                    style = stroke
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(half + gap, 0f),
                    size = Size(half, h),
                    cornerRadius = CornerRadius(3.dp.toPx()),
                    style = stroke
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(0f, half + gap),
                    size = Size(half, half),
                    cornerRadius = CornerRadius(3.dp.toPx()),
                    style = stroke
                )
            }

            MotionNavCategory.VALUES -> {
                // Sliders / Value Wave Icon
                drawLine(
                    color = color,
                    start = Offset(0f, h * 0.25f),
                    end = Offset(w, h * 0.25f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawCircle(
                    color = color,
                    radius = 3.dp.toPx(),
                    center = Offset(w * 0.3f, h * 0.25f)
                )
                drawLine(
                    color = color,
                    start = Offset(0f, h * 0.75f),
                    end = Offset(w, h * 0.75f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawCircle(
                    color = color,
                    radius = 3.dp.toPx(),
                    center = Offset(w * 0.7f, h * 0.75f)
                )
            }

            MotionNavCategory.TRANSITIONS -> {
                // State Machine Connected Nodes Icon
                val r = 2.8.dp.toPx()
                val p1 = Offset(w * 0.2f, h * 0.75f)
                val p2 = Offset(w * 0.5f, h * 0.22f)
                val p3 = Offset(w * 0.8f, h * 0.75f)
                drawLine(color = color, start = p1, end = p2, strokeWidth = 1.8.dp.toPx())
                drawLine(color = color, start = p2, end = p3, strokeWidth = 1.8.dp.toPx())
                drawLine(color = color, start = p1, end = p3, strokeWidth = 1.8.dp.toPx())
                drawCircle(color = color, radius = r, center = p1)
                drawCircle(color = color, radius = r, center = p2)
                drawCircle(color = color, radius = r, center = p3)
            }

            MotionNavCategory.PHYSICS -> {
                // Spring / Orbit Physics Icon
                val path = Path().apply {
                    moveTo(0f, h * 0.5f)
                    cubicTo(w * 0.2f, 0f, w * 0.35f, h, w * 0.5f, h * 0.5f)
                    cubicTo(w * 0.65f, 0f, w * 0.8f, h, w, h * 0.5f)
                }
                drawPath(path = path, color = color, style = stroke)
            }
        }
    }
}
