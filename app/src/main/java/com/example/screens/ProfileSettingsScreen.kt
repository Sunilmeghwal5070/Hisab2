package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.springPressEffect
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NavyDark
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftBlue
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftOrange
import com.example.ui.theme.SoftPurple
import com.example.ui.theme.SoftPurpleBg
import com.example.ui.theme.SoftRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToDataManagement: () -> Unit = {},
    onNavigateToTimeline: () -> Unit = {},
    onNavigateToBalances: () -> Unit = {},
    onNavigateToReminders: () -> Unit = {},
    onNavigateToNotes: () -> Unit = {},
    onNavigateToAttendanceReminders: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }
    var pinCodeInput by remember { mutableStateOf(userProfile.pinCode) }
    var currentLang by remember { mutableStateOf("हिंदी") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. User Profile Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2A16D)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Avatar",
                                    tint = NavyDark,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = userProfile.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = userProfile.email,
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                // 2. Settings Items Card matching reference Screen 10
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            // 1. Language
                            SettingMenuItem(
                                icon = Icons.Default.Language,
                                iconColor = RoyalBlue,
                                title = "Language",
                                value = currentLang,
                                onClick = {
                                    currentLang = if (currentLang == "हिंदी") "English" else "हिंदी"
                                }
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)

                            // 2. Theme
                            SettingMenuItem(
                                icon = Icons.Default.LightMode,
                                iconColor = WarningAmber,
                                title = "Theme",
                                value = "Light",
                                onClick = {}
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)

                            // 3. Currency
                            SettingMenuItem(
                                icon = Icons.Default.CurrencyRupee,
                                iconColor = WarningAmber,
                                title = "Currency",
                                value = "₹ INR",
                                onClick = {}
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)

                            // 4. Security & Privacy
                            SettingMenuItem(
                                icon = Icons.Default.Lock,
                                iconColor = RoyalBlue,
                                title = "Security & Privacy",
                                value = if (userProfile.isPinEnabled) "PIN On" else null,
                                onClick = { showSecurityDialog = true }
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)

                            // 5. Backup & Export
                            SettingMenuItem(
                                icon = Icons.Default.Storage,
                                iconColor = RoyalBlue,
                                title = "Backup & Export",
                                value = null,
                                onClick = onNavigateToDataManagement
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)

                            // 6. Attendance Reminders & Notifications
                            SettingMenuItem(
                                icon = Icons.Default.Notifications,
                                iconColor = RoyalBlue,
                                title = "Attendance Reminders",
                                value = "Active",
                                onClick = onNavigateToAttendanceReminders
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)

                            // 7. Help & Support
                            SettingMenuItem(
                                icon = Icons.Default.HelpOutline,
                                iconColor = RoyalBlue,
                                title = "Help & Support",
                                value = null,
                                onClick = {}
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)

                            // 8. About App
                            SettingMenuItem(
                                icon = Icons.Default.Info,
                                iconColor = RoyalBlue,
                                title = "About App",
                                value = null,
                                onClick = { showAboutDialog = true }
                            )
                        }
                    }
                }

                // 3. Advanced Work & Money Tools
                item {
                    Text(
                        text = "ADVANCED WORK & MONEY TOOLS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            SettingMenuItem(
                                icon = Icons.Default.Timeline,
                                iconColor = RoyalBlue,
                                title = "Smart Financial Timeline",
                                value = null,
                                onClick = onNavigateToTimeline
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)
                            SettingMenuItem(
                                icon = Icons.Default.AccountBalance,
                                iconColor = WarningOrange,
                                title = "Employer Balance Sheet",
                                value = null,
                                onClick = onNavigateToBalances
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)
                            SettingMenuItem(
                                icon = Icons.Default.NotificationsActive,
                                iconColor = EmeraldGreen,
                                title = "Payment Reminders",
                                value = null,
                                onClick = onNavigateToReminders
                            )
                            HorizontalDivider(color = Color(0xFFF0F4FA), thickness = 1.dp)
                            SettingMenuItem(
                                icon = Icons.Default.EventNote,
                                iconColor = SoftPurple,
                                title = "Work Notes & Diary",
                                value = null,
                                onClick = onNavigateToNotes
                            )
                        }
                    }
                }
            }

            // 5. Logout Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .springPressEffect { showLogoutDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoftRed)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = DangerRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DangerRed
                    )
                }
            }
        }
    }

    // Security & PIN Dialog
    if (showSecurityDialog) {
        AlertDialog(
            onDismissRequest = { showSecurityDialog = false },
            title = { Text("Security & PIN Lock", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Enable App PIN Lock", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Protect your records from unauthorized view", fontSize = 11.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = userProfile.isPinEnabled,
                            onCheckedChange = { checked ->
                                viewModel.setPin(checked, pinCodeInput)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = RoyalBlue)
                        )
                    }

                    if (userProfile.isPinEnabled) {
                        OutlinedTextField(
                            value = pinCodeInput,
                            onValueChange = { pinCodeInput = it.take(4) },
                            label = { Text("4-Digit Security PIN") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Biometric Fingerprint", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Unlock using your device fingerprint", fontSize = 11.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = userProfile.isBiometricEnabled,
                            onCheckedChange = { checked ->
                                viewModel.setBiometric(checked)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldGreen)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (userProfile.isPinEnabled) {
                            viewModel.setPin(true, pinCodeInput)
                        }
                        showSecurityDialog = false
                    }
                ) {
                    Text("Save", color = RoyalBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSecurityDialog = false }) {
                    Text("Close", color = TextSecondary)
                }
            }
        )
    }

    // About HISAB Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = RoyalBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("About HISAB", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("HISAB – Attendance & Salary Manager", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                    Text("Tagline: Your Work. Your Money. Your Records.", fontSize = 12.sp, color = RoyalBlue, fontWeight = FontWeight.SemiBold)
                    Text("Promise: Your Complete Work & Money History, Safely Stored on Your Phone.", fontSize = 12.sp, color = EmeraldGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "HISAB is a 100% offline-first application designed for Indian workers, site engineers, contractors and employees to manage attendance, daily wages, payments and accounts with local SQLite reliability.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )
                    Text("Version: 2.0 (Room Database Edition)", fontSize = 11.sp, color = TextSecondary)
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Close", color = RoyalBlue, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to log out from HISAB? Your local data remains securely on your phone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Text("Logout", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingMenuItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    value: String?,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .springPressEffect(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value != null) {
                Text(
                    text = value,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
