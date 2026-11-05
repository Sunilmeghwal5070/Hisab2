package com.example.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.CyanTeal
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NavyDark
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: KaamKhataViewModel
) {
    val isOnboarded by viewModel.isOnboarded.collectAsState()

    val progressAnim = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }
    val contentScale = remember { Animatable(0.85f) }

    fun navigateNext() {
        val destination = if (isOnboarded) "home" else "onboarding"
        navController.navigate(destination) {
            popUpTo("splash") { inclusive = true }
        }
    }

    LaunchedEffect(key1 = true) {
        contentAlpha.animateTo(1f, animationSpec = tween(400))
        contentScale.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))
        progressAnim.animateTo(1f, animationSpec = tween(1800, easing = LinearEasing))
        delay(100)
        navigateNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D2146),
                        Color(0xFF09162E),
                        Color(0xFF050D1C)
                    )
                )
            )
            .clickable { navigateNext() },
        contentAlignment = Alignment.Center
    ) {
        // City Skyline & Construction Crane in background Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Soft glowing blue circles
            drawCircle(
                color = Color(0xFF1E4C8A).copy(alpha = 0.25f),
                radius = 180.dp.toPx(),
                center = Offset(w * 0.5f, h * 0.35f)
            )

            // Skyline silhouette at bottom
            val skyColor = Color(0xFF12284C).copy(alpha = 0.45f)
            drawRect(
                color = skyColor,
                topLeft = Offset(w * 0.05f, h * 0.65f),
                size = Size(50.dp.toPx(), h * 0.35f)
            )
            drawRect(
                color = skyColor,
                topLeft = Offset(w * 0.18f, h * 0.62f),
                size = Size(40.dp.toPx(), h * 0.38f)
            )
            drawRect(
                color = skyColor,
                topLeft = Offset(w * 0.72f, h * 0.60f),
                size = Size(45.dp.toPx(), h * 0.40f)
            )
            drawRect(
                color = skyColor,
                topLeft = Offset(w * 0.85f, h * 0.66f),
                size = Size(40.dp.toPx(), h * 0.34f)
            )

            // Crane silhouette on right
            val cranePath = Path().apply {
                moveTo(w * 0.82f, h * 0.64f)
                lineTo(w * 0.82f, h * 0.52f)
                lineTo(w * 0.68f, h * 0.48f)
                lineTo(w * 0.95f, h * 0.54f)
            }
            drawPath(
                path = cranePath,
                color = Color(0xFF1B3D72).copy(alpha = 0.5f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .alpha(contentAlpha.value)
                .scale(contentScale.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 1. Logo & Branding
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 3D-styled Calendar Logo with Rupee Badge
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // White calendar card with blue top
                    Card(
                        modifier = Modifier
                            .size(105.dp)
                            .shadow(20.dp, RoundedCornerShape(26.dp), spotColor = RoyalBlue),
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column {
                            // Top blue header
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(RoyalBlue, Color(0xFF1A52B8))
                                        )
                                    )
                            )
                            // Calendar grid lines
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = RoyalBlue.copy(alpha = 0.85f),
                                    modifier = Modifier.size(46.dp)
                                )
                            }
                        }
                    }

                    // Emerald Green Circle badge with Rupee Symbol at bottom-right
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(48.dp)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFF22D396), EmeraldGreen, Color(0xFF0F7A56))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CurrencyRupee,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Brand Name
                Text(
                    text = "HISAB",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Tagline
                Text(
                    text = "Your Work. Your Money. Your Records.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            // 2. Character Illustration: Indian Worker / Engineer giving Thumbs Up
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF1F4884).copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Construction helmet + face container
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2A16D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Yellow Safety Hardhat Helmet
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                                    .background(Color(0xFFFFC107)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFA000))
                                )
                            }
                            // Face details
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF14243A)))
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF14243A)))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            // Beard / Smile
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp))
                                    .background(Color(0xFF1E2837)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(modifier = Modifier.width(14.dp).height(3.dp).clip(RoundedCornerShape(2.dp)).background(Color.White))
                            }
                        }
                    }

                    // Orange High-Visibility Safety Vest & Shirt
                    Row(
                        modifier = Modifier
                            .width(150.dp)
                            .height(64.dp)
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .background(Color(0xFFFF6D00)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.width(22.dp).height(64.dp).background(Color(0xFF1E88E5)))
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(64.dp)
                                .background(Color(0xFF1565C0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.width(12.dp).height(50.dp).background(Color.White))
                        }
                        // Thumbs up badge on right
                        Box(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFA000)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ThumbUp,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // 3. Bottom Loading Bar & Text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressAnim.value)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(RoyalBlue, CyanTeal)
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Loading...",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
