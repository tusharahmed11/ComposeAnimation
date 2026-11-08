package com.imtusharahmed.composeanimation.ui.screens.shared

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateBounds
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtusharahmed.composeanimation.ui.common.LabActionButton
import com.imtusharahmed.composeanimation.ui.common.LocalAnimationSpeed
import com.imtusharahmed.composeanimation.ui.common.MotionCard
import com.imtusharahmed.composeanimation.ui.common.MotionStage
import com.imtusharahmed.composeanimation.ui.common.scaledDuration
import com.imtusharahmed.composeanimation.ui.theme.CyberViolet
import com.imtusharahmed.composeanimation.ui.theme.ElectricBlue
import com.imtusharahmed.composeanimation.ui.theme.ElectricCyan
import com.imtusharahmed.composeanimation.ui.theme.NeonGreen
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBase
import com.imtusharahmed.composeanimation.ui.theme.ObsidianBorder
import com.imtusharahmed.composeanimation.ui.theme.ObsidianElevated
import com.imtusharahmed.composeanimation.ui.theme.RadiantSunset
import com.imtusharahmed.composeanimation.ui.theme.TextPrimary
import com.imtusharahmed.composeanimation.ui.theme.TextSecondary

@Composable
fun LookaheadAndSharedScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        SharedTransitionLayoutCard()
        LookaheadScopeAdaptiveCard()
    }
}

/* =======================================================================
   1. SharedTransitionLayout: Master-Detail sharedElement & sharedBounds
   ======================================================================= */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionLayoutCard() {
    var isDetailView by remember { mutableStateOf(false) }
    val speed = LocalAnimationSpeed.current
    val animDuration = scaledDuration(550, speed)

    MotionCard(
        title = "SharedTransition Master-Detail",
        apiTag = "SharedTransitionLayout",
        subtitle = "Continuous spatial interpolation across sharedElement (hero) and sharedBounds (title & price)",
        accentColor = ElectricBlue
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = isDetailView,
                    transitionSpec = {
                        fadeIn(tween(animDuration)) togetherWith fadeOut(tween(animDuration))
                    },
                    label = "sharedMasterDetail"
                ) { inDetail ->
                    if (inDetail) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(ObsidianBase)
                                .border(1.5.dp, CyberViolet, RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .sharedElement(
                                                sharedContentState = rememberSharedContentState(key = "product_hero_img"),
                                                animatedVisibilityScope = this@AnimatedContent
                                            )
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                Brush.radialGradient(listOf(ElectricCyan, CyberViolet))
                                            )
                                            .border(2.dp, Color.White, RoundedCornerShape(20.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("⚡", fontSize = 26.sp)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .sharedBounds(
                                                sharedContentState = rememberSharedContentState(key = "product_price_tag"),
                                                animatedVisibilityScope = this@AnimatedContent
                                            )
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NeonGreen.copy(alpha = 0.2f))
                                            .border(1.dp, NeonGreen, RoundedCornerShape(10.dp))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "$499.00 USD",
                                            color = NeonGreen,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Box(
                                    modifier = Modifier
                                        .sharedBounds(
                                            sharedContentState = rememberSharedContentState(key = "product_title_label"),
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                ) {
                                    Text(
                                        text = "CYBERNETIC MOTION ACCELERATOR",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Hardware-synchronized lookahead pass interpolates size, position, and corner geometry between arbitrary hierarchy trees.",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                LabActionButton(
                                    text = "← Collapse to Compact Card",
                                    onClick = { isDetailView = false },
                                    modifier = Modifier.fillMaxWidth(),
                                    color = ElectricCyan
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(ObsidianBase)
                                .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp))
                                .clickable { isDetailView = true }
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .sharedElement(
                                                sharedContentState = rememberSharedContentState(key = "product_hero_img"),
                                                animatedVisibilityScope = this@AnimatedContent
                                            )
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                Brush.radialGradient(listOf(ElectricCyan, CyberViolet))
                                            )
                                            .border(1.dp, Color.White, RoundedCornerShape(14.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("⚡", fontSize = 20.sp)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .sharedBounds(
                                                    sharedContentState = rememberSharedContentState(key = "product_title_label"),
                                                    animatedVisibilityScope = this@AnimatedContent
                                                )
                                        ) {
                                            Text(
                                                text = "MOTION ACCELERATOR",
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "Tap card to expand shared hero →",
                                            color = ElectricCyan,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .sharedBounds(
                                            sharedContentState = rememberSharedContentState(key = "product_price_tag"),
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(NeonGreen.copy(alpha = 0.2f))
                                        .border(1.dp, NeonGreen, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "$499",
                                        color = NeonGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
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

/* =======================================================================
   2. LookaheadScope: Pre-Calculated Target Position Sibling Choreography
   ======================================================================= */
@Composable
private fun LookaheadScopeAdaptiveCard() {
    var isRowLayout by remember { mutableStateOf(false) }

    MotionCard(
        title = "LookaheadScope Adaptive Layout",
        apiTag = "LookaheadScope + Modifier.animateBounds",
        subtitle = "Pre-calculated layout pass where sibling items smoothly morph between Column & Row",
        accentColor = ElectricCyan
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MotionStage(height = 215.dp) {
                LookaheadScope {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isRowLayout) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AdaptiveSibling(
                                    title = "Quantum",
                                    subtitle = "01",
                                    color = ElectricCyan,
                                    isRow = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(145.dp)
                                        .animateBounds(this@LookaheadScope)
                                )
                                AdaptiveSibling(
                                    title = "Neural",
                                    subtitle = "02",
                                    color = CyberViolet,
                                    isRow = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(145.dp)
                                        .animateBounds(this@LookaheadScope)
                                )
                                AdaptiveSibling(
                                    title = "Optical",
                                    subtitle = "03",
                                    color = RadiantSunset,
                                    isRow = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(145.dp)
                                        .animateBounds(this@LookaheadScope)
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AdaptiveSibling(
                                    title = "Quantum Core Processor",
                                    subtitle = "NODE 01",
                                    color = ElectricCyan,
                                    isRow = false,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .animateBounds(this@LookaheadScope)
                                )
                                AdaptiveSibling(
                                    title = "Neural Pipeline Engine",
                                    subtitle = "NODE 02",
                                    color = CyberViolet,
                                    isRow = false,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .animateBounds(this@LookaheadScope)
                                )
                                AdaptiveSibling(
                                    title = "Optical Wave Accelerator",
                                    subtitle = "NODE 03",
                                    color = RadiantSunset,
                                    isRow = false,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .animateBounds(this@LookaheadScope)
                                )
                            }
                        }
                    }
                }
            }

            LabActionButton(
                text = if (isRowLayout) "Morph to Stacked Column Layout" else "Morph to 3-Column Bento Grid",
                onClick = { isRowLayout = !isRowLayout },
                modifier = Modifier.fillMaxWidth(),
                color = if (isRowLayout) CyberViolet else ElectricCyan
            )
        }
    }
}

@Composable
private fun AdaptiveSibling(
    title: String,
    subtitle: String,
    color: Color,
    isRow: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(ObsidianElevated)
            .border(1.5.dp, color.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isRow) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.25f))
                        .border(1.dp, color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = subtitle,
                        color = color,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = subtitle,
                    color = color,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
