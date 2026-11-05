package com.example.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Adds an elegant, subtle moving light reflection (shimmer sweep)
 * across the component, creating a premium financial app feel.
 */
fun Modifier.shimmerShine(
    enabled: Boolean = true,
    durationMillis: Int = 3000,
    shineColor: Color = Color.White.copy(alpha = 0.25f)
): Modifier = composed {
    if (!enabled) return@composed this

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val progress by transition.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    this.drawWithContent {
        drawContent()

        val width = size.width
        val height = size.height

        val startX = progress * (width * 1.8f) - (width * 0.4f)
        val endX = startX + width * 0.45f

        val shineBrush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                shineColor.copy(alpha = shineColor.alpha * 0.3f),
                shineColor,
                shineColor.copy(alpha = shineColor.alpha * 0.3f),
                Color.Transparent
            ),
            start = Offset(startX, 0f),
            end = Offset(endX, height)
        )

        drawRect(
            brush = shineBrush,
            blendMode = BlendMode.SrcAtop
        )
    }
}

/**
 * Tactile spring press effect that scales the component slightly down (0.96f)
 * on touch down and smoothly springs back upon release.
 */
fun Modifier.springPressEffect(
    pressedScale: Float = 0.96f,
    onClick: (() -> Unit)? = null
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = 0.65f,
            stiffness = 700f
        ),
        label = "springPressScale"
    )

    val modifier = this.scale(scale)
    if (onClick != null) {
        modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    } else {
        modifier
    }
}
