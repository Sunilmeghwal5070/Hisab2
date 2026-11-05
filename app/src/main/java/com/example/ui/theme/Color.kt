package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Reference Colors
val MidnightNavy = Color(0xFF06132F)
val RichNavy = Color(0xFF0B1F45)
val NavyDark = Color(0xFF06132F)
val NavyHeader = Color(0xFF0B1F45)
val RoyalBlue = Color(0xFF2563EB)
val RoyalBlueLight = Color(0xFF397BFF)
val ElectricBlue = Color(0xFF397BFF)
val PremiumCyan = Color(0xFF22C6D3)
val CyanTeal = Color(0xFF22C6D3)
val TealGradient = Color(0xFF19BBAA)
val EmeraldGreen = Color(0xFF16A982)
val DangerRed = Color(0xFFE65353)
val WarningOrange = Color(0xFFF3B43F)
val WarningAmber = Color(0xFFF3B43F)
val SoftPurple = Color(0xFF8B5CF6)
val SoftPurpleBg = Color(0xFFF5F0FF)

// Neutrals & Surfaces
val BackgroundLight = Color(0xFFF4F7FC)
val Surface = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF17243B)
val TextSecondary = Color(0xFF8793A6)
val CardBorder = Color(0xFFE2E8F0)

// Soft Accent Backgrounds
val SoftGreen = Color(0xFFE8F8F2)
val SoftRed = Color(0xFFFDEEEC)
val SoftOrange = Color(0xFFFEF5E7)
val SoftBlue = Color(0xFFEEF3FF)

// Gradients
val HeroGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF1D4ED8),
        Color(0xFF2563EB),
        Color(0xFF0284C7),
        Color(0xFF14B8A6)
    )
)

val HeaderNavyGradient = Brush.verticalGradient(
    colors = listOf(MidnightNavy, RichNavy)
)

val ButtonNavyGradient = Brush.horizontalGradient(
    colors = listOf(RichNavy, RoyalBlue)
)

val ButtonBlueGradient = Brush.horizontalGradient(
    colors = listOf(ElectricBlue, RoyalBlue)
)

val SplashGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF0B1E40), Color(0xFF06132F), Color(0xFF040C1E))
)

