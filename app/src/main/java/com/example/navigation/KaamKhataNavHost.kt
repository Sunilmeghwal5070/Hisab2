package com.example.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.example.components.springPressEffect
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
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.components.KaamKhataBottomBar
import com.example.screens.AddJobScreen
import com.example.screens.AiAssistantScreen
import com.example.screens.AttendanceRemindersSettingsScreen
import com.example.screens.AttendanceScreen
import com.example.screens.HomeScreen
import com.example.screens.OnboardingScreen
import com.example.screens.PaymentsScreen
import com.example.screens.DataManagementScreen
import com.example.screens.EmployerBalanceScreen
import com.example.screens.FinancialTimelineScreen
import com.example.screens.PaymentRemindersScreen
import com.example.screens.ProfileSettingsScreen
import com.example.screens.ReportsScreen
import com.example.screens.SplashScreen
import com.example.screens.WorkNotesScreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftBlue
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftOrange
import com.example.ui.theme.SoftPurple
import com.example.ui.theme.SoftPurpleBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaamKhataNavHost(
    viewModel: KaamKhataViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "splash"

    val userProfile by viewModel.userProfile.collectAsState()
    val isAppUnlocked by viewModel.isAppUnlocked.collectAsState()

    var showFabMenuSheet by remember { mutableStateOf(false) }
    val fabSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Main tabs where bottom navigation bar is shown
    val bottomBarRoutes = listOf("home", "attendance", "reports", "settings")

    // Security PIN Lock check
    val needsPinLock = userProfile.isPinEnabled && !isAppUnlocked && currentRoute != "splash" && currentRoute != "onboarding"
    if (needsPinLock) {
        PinUnlockDialog(
            userProfile = userProfile,
            onUnlock = { pin -> viewModel.unlockWithPin(pin) },
            onBiometricUnlock = { viewModel.unlockWithBiometric() }
        )
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                KaamKhataBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onFabClick = { showFabMenuSheet = true }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "splash",
                enterTransition = { slideInHorizontally(initialOffsetX = { 60 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -60 }, animationSpec = tween(250)) + fadeOut(animationSpec = tween(250)) },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -60 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 60 }, animationSpec = tween(250)) + fadeOut(animationSpec = tween(250)) }
            ) {
                // 1. Splash Screen
                composable("splash") {
                    SplashScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                // 2 & 3. Onboarding Screen
                composable("onboarding") {
                    OnboardingScreen(
                        viewModel = viewModel,
                        onOnboardingComplete = {
                            navController.navigate("home") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    )
                }

                // 4. Home Dashboard
                composable("home") {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToAttendance = { navController.navigate("attendance") },
                        onNavigateToSalaryPayments = { navController.navigate("salary_payments") },
                        onNavigateToAddJob = { navController.navigate("add_job") },
                        onNavigateToMonthlyHisab = { navController.navigate("reports") },
                        onNavigateToAiAssistant = { navController.navigate("ai_assistant") },
                        onNavigateToDataManagement = { navController.navigate("data_management") },
                        onNavigateToTimeline = { navController.navigate("financial_timeline") },
                        onNavigateToBalances = { navController.navigate("employer_balances") },
                        onNavigateToReminders = { navController.navigate("payment_reminders") },
                        onNavigateToNotes = { navController.navigate("work_notes") }
                    )
                }

                // 5. Attendance Calendar
                composable("attendance") {
                    AttendanceScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 6. Add Job
                composable("add_job") {
                    AddJobScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 7. Salary & Payments
                composable("salary_payments") {
                    PaymentsScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 8. Monthly Hisab
                composable("reports") {
                    ReportsScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 9. AI Assistant
                composable("ai_assistant") {
                    AiAssistantScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 10. Settings / Profile
                composable("settings") {
                    ProfileSettingsScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onLogout = {
                            viewModel.logout()
                            navController.navigate("onboarding") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        onNavigateToDataManagement = { navController.navigate("data_management") },
                        onNavigateToTimeline = { navController.navigate("financial_timeline") },
                        onNavigateToBalances = { navController.navigate("employer_balances") },
                        onNavigateToReminders = { navController.navigate("payment_reminders") },
                        onNavigateToNotes = { navController.navigate("work_notes") },
                        onNavigateToAttendanceReminders = { navController.navigate("attendance_reminders") }
                    )
                }

                // 10.5 Attendance Reminders Settings
                composable("attendance_reminders") {
                    AttendanceRemindersSettingsScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 11. Data Management (Export / Import / Backup / Diagnostics)
                composable("data_management") {
                    DataManagementScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 12. Smart Financial Timeline
                composable("financial_timeline") {
                    FinancialTimelineScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 13. Employer-Wise Balance Sheet
                composable("employer_balances") {
                    EmployerBalanceScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 14. Multiple Payment Reminders
                composable("payment_reminders") {
                    PaymentRemindersScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 15. Work Notes & Diary
                composable("work_notes") {
                    WorkNotesScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }

    // Floating (+) Action Menu Bottom Sheet
    if (showFabMenuSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFabMenuSheet = false },
            sheetState = fabSheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Quick Actions",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FabMenuActionCard(
                        title = "Mark Attendance",
                        icon = Icons.Default.Check,
                        color = EmeraldGreen,
                        bg = SoftGreen,
                        onClick = {
                            showFabMenuSheet = false
                            navController.navigate("attendance")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    FabMenuActionCard(
                        title = "Add Payment",
                        icon = Icons.Default.Payments,
                        color = RoyalBlue,
                        bg = SoftBlue,
                        onClick = {
                            showFabMenuSheet = false
                            navController.navigate("salary_payments")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FabMenuActionCard(
                        title = "Add Job",
                        icon = Icons.Default.Business,
                        color = WarningOrange,
                        bg = SoftOrange,
                        onClick = {
                            showFabMenuSheet = false
                            navController.navigate("add_job")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    FabMenuActionCard(
                        title = "AI Assistant",
                        icon = Icons.Default.AutoAwesome,
                        color = SoftPurple,
                        bg = SoftPurpleBg,
                        onClick = {
                            showFabMenuSheet = false
                            navController.navigate("ai_assistant")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun FabMenuActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    bg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.springPressEffect(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun PinUnlockDialog(
    userProfile: com.example.data.models.UserProfile,
    onUnlock: (String) -> Boolean,
    onBiometricUnlock: () -> Unit
) {
    var enteredPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { /* Modal lock - cannot dismiss without pin */ },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SoftBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "HISAB Security Lock",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Enter 4-digit PIN to access your data",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 4 PIN indicator dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { idx ->
                        val filled = idx < enteredPin.length
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isError) com.example.ui.theme.DangerRed
                                    else if (filled) RoyalBlue
                                    else Color(0xFFE2E8F0)
                                )
                        )
                    }
                }

                if (isError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Incorrect PIN. Please try again.",
                        fontSize = 11.sp,
                        color = com.example.ui.theme.DangerRed,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Keypad 1-9, 0, Backspace, Biometric
                val keys = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("bio", "0", "del")
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    keys.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { key ->
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(if (key == "bio" && !userProfile.isBiometricEnabled) Color.Transparent else Color(0xFFF1F5F9))
                                        .clickable {
                                            when (key) {
                                                "del" -> {
                                                    if (enteredPin.isNotEmpty()) {
                                                        enteredPin = enteredPin.dropLast(1)
                                                        isError = false
                                                    }
                                                }
                                                "bio" -> {
                                                    if (userProfile.isBiometricEnabled) {
                                                        onBiometricUnlock()
                                                    }
                                                }
                                                else -> {
                                                    if (enteredPin.length < 4) {
                                                        val next = enteredPin + key
                                                        enteredPin = next
                                                        isError = false
                                                        if (next.length == 4) {
                                                            val success = onUnlock(next)
                                                            if (!success) {
                                                                isError = true
                                                                enteredPin = ""
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    when (key) {
                                        "del" -> Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = "Delete", tint = TextPrimary, modifier = Modifier.size(20.dp))
                                        "bio" -> if (userProfile.isBiometricEnabled) Icon(Icons.Default.Fingerprint, contentDescription = "Biometric", tint = RoyalBlue, modifier = Modifier.size(24.dp))
                                        else -> Text(text = key, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
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

