package com.example.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NavyDark
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.RoyalBlueLight

@Composable
fun KaamKhataBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onFabClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Main Navy Bar with premium rounded top and shadow
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = NavyDark,
            shadowElevation = 24.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Item 1: Home
                BottomNavItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    isSelected = currentRoute == "home",
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigate("home")
                    },
                    modifier = Modifier.weight(1f)
                )

                // Item 2: Attendance
                BottomNavItem(
                    icon = Icons.Default.CalendarMonth,
                    label = "Attendance",
                    isSelected = currentRoute == "attendance",
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigate("attendance")
                    },
                    modifier = Modifier.weight(1f)
                )

                // Center Spacer for Floating (+) button
                Box(modifier = Modifier.weight(1f))

                // Item 4: Reports
                BottomNavItem(
                    icon = Icons.Default.Assessment,
                    label = "Reports",
                    isSelected = currentRoute == "reports",
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigate("reports")
                    },
                    modifier = Modifier.weight(1f)
                )

                // Item 5: Profile / Settings
                BottomNavItem(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    isSelected = currentRoute == "settings",
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigate("settings")
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Center Floating Action Button (+) anchored securely above the navigation bar
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
                .size(56.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    spotColor = RoyalBlue,
                    ambientColor = RoyalBlue
                )
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF3B82F6), RoyalBlue, Color(0xFF1D4ED8))
                    )
                )
                .shimmerShine(durationMillis = 3500)
                .springPressEffect(pressedScale = 0.92f) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onFabClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Quick Action",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.18f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "navIconScale"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .size(22.dp)
                .scale(iconScale)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
            modifier = Modifier.padding(top = 2.dp)
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .size(width = 16.dp, height = 3.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(com.example.ui.theme.PremiumCyan)
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}
