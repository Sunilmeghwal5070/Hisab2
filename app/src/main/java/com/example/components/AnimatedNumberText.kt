package com.example.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

private val indianLocale = Locale("en", "IN")
private val currencyFormat = NumberFormat.getNumberInstance(indianLocale)

@Composable
fun AnimatedRupeeText(
    amount: Double,
    modifier: Modifier = Modifier,
    prefix: String = "₹",
    style: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    color: Color = Color.Unspecified
) {
    val targetInt = amount.roundToInt()
    val animatedValue by animateIntAsState(
        targetValue = targetInt,
        animationSpec = tween(
            durationMillis = 850,
            easing = FastOutSlowInEasing
        ),
        label = "rupeeCounter"
    )

    val formatted = currencyFormat.format(animatedValue)

    Text(
        text = "$prefix$formatted",
        style = style,
        color = color,
        modifier = modifier
    )
}

@Composable
fun AnimatedCountText(
    count: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    color: Color = Color.Unspecified
) {
    val animatedCount by animateIntAsState(
        targetValue = count,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = "intCounter"
    )

    Text(
        text = animatedCount.toString(),
        style = style,
        color = color,
        modifier = modifier
    )
}
