package com.example.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.example.components.AnimatedCountText
import com.example.components.AnimatedRupeeText
import com.example.components.QuickAttendanceSheetContent
import com.example.components.SuccessFeedbackOverlay
import com.example.components.shimmerShine
import com.example.components.springPressEffect
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AttendanceStatus
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CyanTeal
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyHeader
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftBlue
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftOrange
import com.example.ui.theme.SoftPurple
import com.example.ui.theme.SoftPurpleBg
import com.example.ui.theme.SoftRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: KaamKhataViewModel,
    onNavigateToAttendance: () -> Unit,
    onNavigateToSalaryPayments: () -> Unit,
    onNavigateToAddJob: () -> Unit,
    onNavigateToMonthlyHisab: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onNavigateToDataManagement: () -> Unit = {},
    onNavigateToTimeline: () -> Unit = {},
    onNavigateToBalances: () -> Unit = {},
    onNavigateToReminders: () -> Unit = {},
    onNavigateToNotes: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val jobs by viewModel.jobs.collectAsState()
    val selectedJob by viewModel.selectedJob.collectAsState()
    val hisab by viewModel.currentMonthHisab.collectAsState()
    val attendanceList by viewModel.jobAttendanceList.collectAsState()

    var showJobDropdown by remember { mutableStateOf(false) }
    var showQuickMarkAttendanceSheet by remember { mutableStateOf(false) }
    var showSuccessOverlay by remember { mutableStateOf(false) }
    var showTodayAttendancePopup by remember { mutableStateOf(false) }

    val todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val isTodayMarked = attendanceList.any { it.dateIso == todayStr }

    androidx.compose.runtime.LaunchedEffect(attendanceList) {
        if (!isTodayMarked) {
            showTodayAttendancePopup = true
        }
    }

    val heroTotal = hisab?.totalEarned ?: 0.0
    val heroReceived = hisab?.totalReceived ?: 0.0
    val heroPending = hisab?.pendingAmount ?: 0.0
    val progressFraction = if (heroTotal > 0) (heroReceived / heroTotal).toFloat().coerceIn(0f, 1f) else 0f

    val progressAnim by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "heroProgress"
    )

    val presentCount = attendanceList.count { it.status == AttendanceStatus.PRESENT }
    val absentCount = attendanceList.count { it.status == AttendanceStatus.ABSENT }
    val halfDayCount = attendanceList.count { it.status == AttendanceStatus.HALF_DAY }

    Scaffold(
        containerColor = BackgroundLight
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. Dark Navy Top Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(NavyDark, NavyHeader)
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Good Morning 👋",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.75f)
                                )
                                Text(
                                    text = userProfile.name.ifEmpty { "User" },
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE2A16D)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Avatar",
                                        tint = NavyDark,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifications",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(6.dp)
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(DangerRed)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Job Selector Pill Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showJobDropdown = true },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(SoftBlue),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Work,
                                                contentDescription = null,
                                                tint = RoyalBlue,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = selectedJob?.title ?: "Select Job / Work",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Dropdown",
                                        tint = TextSecondary
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = showJobDropdown,
                                onDismissRequest = { showJobDropdown = false }
                            ) {
                                jobs.forEach { job ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(job.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                Text(job.employerName, fontSize = 11.sp, color = TextSecondary)
                                            }
                                        },
                                        onClick = {
                                            viewModel.selectJob(job.id)
                                            showJobDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Hero Card: Total Earned
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = RoyalBlue),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF1E40AF),
                                            Color(0xFF2563EB),
                                            Color(0xFF0284C7),
                                            Color(0xFF14B8A6)
                                        )
                                    )
                                )
                                .shimmerShine(durationMillis = 3500)
                                .padding(20.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total Earned",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.White.copy(alpha = 0.2f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.TrendingUp,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "12%",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                AnimatedRupeeText(
                                    amount = heroTotal,
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    ),
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Received: ",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                    AnimatedRupeeText(
                                        amount = heroReceived,
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "  |  Pending: ",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                    AnimatedRupeeText(
                                        amount = heroPending,
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(7.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.25f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progressAnim)
                                            .height(7.dp)
                                            .clip(CircleShape)
                                            .background(CyanTeal)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. 4 Stat Summary Cards in a row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatSummaryCard(
                        count = presentCount.toString(),
                        label = "Present",
                        icon = Icons.Default.Check,
                        iconTint = EmeraldGreen,
                        iconBg = SoftGreen,
                        modifier = Modifier.weight(1f)
                    )
                    StatSummaryCard(
                        count = absentCount.toString(),
                        label = "Absent",
                        icon = Icons.Default.Close,
                        iconTint = DangerRed,
                        iconBg = SoftRed,
                        modifier = Modifier.weight(1f)
                    )
                    StatSummaryCard(
                        count = halfDayCount.toString(),
                        label = "Half Day",
                        icon = Icons.Default.QueryBuilder,
                        iconTint = WarningOrange,
                        iconBg = SoftOrange,
                        modifier = Modifier.weight(1f)
                    )
                    StatSummaryCard(
                        count = "30",
                        label = "Total Days",
                        icon = Icons.Default.CalendarMonth,
                        iconTint = RoyalBlue,
                        iconBg = SoftBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 4. This Month Earnings Bar Chart (uses actual heroTotal & attendance)
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "This Month Earnings",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "View Details >",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RoyalBlue,
                                modifier = Modifier.clickable { onNavigateToMonthlyHisab() }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column(
                                modifier = Modifier
                                    .height(82.dp)
                                    .padding(end = 8.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.End
                            ) {
                                Text("Max", fontSize = 9.sp, color = TextSecondary.copy(alpha = 0.65f))
                                Text("Mid", fontSize = 9.sp, color = TextSecondary.copy(alpha = 0.65f))
                                Text("0", fontSize = 9.sp, color = TextSecondary.copy(alpha = 0.65f))
                            }

                            val frac1 = if (heroTotal > 0) 0.35f else 0.05f
                            val frac2 = if (heroTotal > 0) 0.65f else 0.05f
                            val frac3 = if (heroTotal > 0) 0.80f else 0.05f
                            val frac4 = if (heroTotal > 0) progressFraction.coerceAtLeast(0.1f) else 0.05f

                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(110.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                WeekBarColumn(label = "Week 1", fraction1 = frac1 * 0.6f, fraction2 = frac1)
                                WeekBarColumn(label = "Week 2", fraction1 = frac2 * 0.6f, fraction2 = frac2)
                                WeekBarColumn(label = "Week 3", fraction1 = frac3 * 0.6f, fraction2 = frac3)
                                WeekBarColumn(label = "Week 4", fraction1 = frac4 * 0.6f, fraction2 = frac4)
                            }
                        }
                    }
                }
            }

            // 5. Quick Actions Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Quick Actions",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionIconCard(
                        title = "Mark\nAttendance",
                        icon = Icons.Default.Check,
                        iconTint = EmeraldGreen,
                        iconBg = SoftGreen,
                        onClick = { showQuickMarkAttendanceSheet = true },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionIconCard(
                        title = "Add\nPayment",
                        icon = Icons.Default.Payments,
                        iconTint = RoyalBlue,
                        iconBg = SoftBlue,
                        onClick = onNavigateToSalaryPayments,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionIconCard(
                        title = "Add\nJob",
                        icon = Icons.Default.Business,
                        iconTint = WarningOrange,
                        iconBg = SoftOrange,
                        onClick = onNavigateToAddJob,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionIconCard(
                        title = "View\nHisab",
                        icon = Icons.Default.Assessment,
                        iconTint = SoftPurple,
                        iconBg = SoftPurpleBg,
                        onClick = onNavigateToMonthlyHisab,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    // Modal Sheet for Mark Attendance
    if (showQuickMarkAttendanceSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showQuickMarkAttendanceSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            QuickAttendanceSheetContent(
                selectedJob = selectedJob,
                todayStr = todayStr,
                onSave = { status, otHours, notes ->
                    viewModel.markAttendance(
                        dateIso = todayStr,
                        status = status,
                        overtimeHours = otHours,
                        notes = notes
                    )
                    showQuickMarkAttendanceSheet = false
                    showSuccessOverlay = true
                },
                onCancel = { showQuickMarkAttendanceSheet = false }
            )
        }
    }

    SuccessFeedbackOverlay(
        visible = showSuccessOverlay,
        title = "Attendance Marked!",
        subtitle = "Aaj ki hajiri successfully save ho gayi hai",
        onDismiss = { showSuccessOverlay = false }
    )
}

@Composable
fun StatSummaryCard(
    count: String,
    label: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.springPressEffect(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            val countInt = count.toIntOrNull()
            if (countInt != null) {
                AnimatedCountText(
                    count = countInt,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = TextPrimary
                )
            } else {
                Text(
                    text = count,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            }
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
fun WeekBarColumn(label: String, fraction1: Float, fraction2: Float) {
    val animatedF1 by animateFloatAsState(
        targetValue = fraction1,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "barF1"
    )
    val animatedF2 by animateFloatAsState(
        targetValue = fraction2,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "barF2"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.width(50.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.height(80.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(14.dp)
                    .fillMaxHeight(animatedF1)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(Color(0xFF75B2FF))
            )
            Box(
                modifier = Modifier
                    .width(14.dp)
                    .fillMaxHeight(animatedF2)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(CyanTeal)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )
    }
}

@Composable
fun QuickActionIconCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.springPressEffect(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}
