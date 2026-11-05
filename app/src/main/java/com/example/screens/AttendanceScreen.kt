package com.example.screens

import com.example.components.AnimatedCountText
import com.example.components.QuickAttendanceSheetContent
import com.example.components.SuccessFeedbackOverlay
import com.example.components.springPressEffect
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.QuickAttendanceSheetContent
import com.example.data.models.Attendance
import com.example.data.models.AttendanceStatus
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftBlue
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftOrange
import com.example.ui.theme.SoftRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit = {}
) {
    val selectedJob by viewModel.selectedJob.collectAsState()
    val attendanceList by viewModel.jobAttendanceList.collectAsState()

    val currentYearMonth = remember { java.time.YearMonth.now() }
    val today = java.time.LocalDate.now()
    var selectedDay by remember { mutableStateOf(if (today.year == currentYearMonth.year && today.month == currentYearMonth.month) today.dayOfMonth else 1) }
    var showEditSheet by remember { mutableStateOf(false) }
    var showSuccessOverlay by remember { mutableStateOf(false) }

    val attendanceMap = attendanceList.associateBy { it.dateIso }
    val monthPrefix = String.format("%04d-%02d", currentYearMonth.year, currentYearMonth.monthValue)
    val monthAttendance = attendanceList.filter { it.dateIso.startsWith(monthPrefix) }
    val presentCount = monthAttendance.count { it.status == AttendanceStatus.PRESENT }
    val absentCount = monthAttendance.count { it.status == AttendanceStatus.ABSENT }
    val halfDayCount = monthAttendance.count { it.status == AttendanceStatus.HALF_DAY }

    val monthTitle = currentYearMonth.format(java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.getDefault()))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Attendance",
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
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendar",
                            tint = RoyalBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Two Filter Dropdowns
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilterPill(title = monthTitle, modifier = Modifier.weight(1f))
                    FilterPill(title = "This Month", modifier = Modifier.weight(1f))
                }
            }

            // 2. Calendar Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Weekday labels (Sun, Mon, Tue, Wed, Thu, Fri, Sat)
                        val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            weekdays.forEach { dayName ->
                                Text(
                                    text = dayName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Dynamic Calendar Grid calculation
                        val firstDayOfWeek = currentYearMonth.atDay(1).dayOfWeek.value % 7
                        val lengthOfMonth = currentYearMonth.lengthOfMonth()
                        val prevMonth = currentYearMonth.minusMonths(1)
                        val lengthOfPrevMonth = prevMonth.lengthOfMonth()

                        val daysGrid = mutableListOf<List<Pair<Int, Boolean>>>()
                        val currentWeek = mutableListOf<Pair<Int, Boolean>>()

                        for (i in (firstDayOfWeek - 1) downTo 0) {
                            currentWeek.add(Pair(lengthOfPrevMonth - i, false))
                        }

                        for (day in 1..lengthOfMonth) {
                            currentWeek.add(Pair(day, true))
                            if (currentWeek.size == 7) {
                                daysGrid.add(currentWeek.toList())
                                currentWeek.clear()
                            }
                        }

                        var nextMonthDay = 1
                        while (currentWeek.isNotEmpty() && currentWeek.size < 7) {
                            currentWeek.add(Pair(nextMonthDay++, false))
                        }
                        if (currentWeek.isNotEmpty()) {
                            daysGrid.add(currentWeek.toList())
                        }

                        daysGrid.forEach { rowDays ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                rowDays.forEach { (day, isCurrentMonth) ->
                                    val dateStr = if (isCurrentMonth) String.format("%04d-%02d-%02d", currentYearMonth.year, currentYearMonth.monthValue, day) else ""
                                    val att = if (isCurrentMonth) attendanceMap[dateStr] else null
                                    val isSelected = isCurrentMonth && day == selectedDay
                                    val isToday = isCurrentMonth && day == today.dayOfMonth && currentYearMonth.year == today.year && currentYearMonth.month == today.month

                                    CalendarDayCircle(
                                        day = day,
                                        isCurrentMonth = isCurrentMonth,
                                        attendance = att,
                                        isSelected = isSelected,
                                        isToday = isToday,
                                        onClick = {
                                            if (isCurrentMonth) {
                                                selectedDay = day
                                                showEditSheet = true
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Attendance Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Attendance Summary",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SummaryStatBox(
                                count = presentCount.toString(),
                                label = "Present",
                                color = EmeraldGreen,
                                bg = SoftGreen,
                                modifier = Modifier.weight(1f)
                            )
                            SummaryStatBox(
                                count = absentCount.toString(),
                                label = "Absent",
                                color = DangerRed,
                                bg = SoftRed,
                                modifier = Modifier.weight(1f)
                            )
                            SummaryStatBox(
                                count = halfDayCount.toString(),
                                label = "Half Day",
                                color = WarningOrange,
                                bg = SoftOrange,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 4. Selected Day Detail Card
            item {
                val selectedDateStr = String.format("%04d-%02d-%02d", currentYearMonth.year, currentYearMonth.monthValue, selectedDay)
                val selectedAtt = attendanceMap[selectedDateStr]
                val formattedDisplayDate = try {
                    java.time.LocalDate.parse(selectedDateStr).format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy • EEEE", java.util.Locale.getDefault()))
                } catch (e: Exception) {
                    "$selectedDay ${monthTitle}"
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formattedDisplayDate,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Job: ${selectedJob?.title ?: "No Job Assigned"}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )

                            if (selectedAtt != null) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when (selectedAtt.status) {
                                                AttendanceStatus.PRESENT -> SoftGreen
                                                AttendanceStatus.ABSENT -> SoftRed
                                                AttendanceStatus.HALF_DAY -> SoftOrange
                                                else -> SoftBlue
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = selectedAtt.status.labelEn,
                                        color = when (selectedAtt.status) {
                                            AttendanceStatus.PRESENT -> EmeraldGreen
                                            AttendanceStatus.ABSENT -> DangerRed
                                            AttendanceStatus.HALF_DAY -> WarningOrange
                                            else -> RoyalBlue
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Working Hours: ${selectedAtt?.workingHours ?: "Not Marked"}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Earnings: ₹${selectedAtt?.earnings?.toInt() ?: 0}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Mark / Edit →",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue,
                                modifier = Modifier.clickable { showEditSheet = true }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showEditSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val selectedDateStr = String.format("%04d-%02d-%02d", currentYearMonth.year, currentYearMonth.monthValue, selectedDay)
        ModalBottomSheet(
            onDismissRequest = { showEditSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            QuickAttendanceSheetContent(
                selectedJob = selectedJob,
                todayStr = selectedDateStr,
                onSave = { status, otHours, notes ->
                    viewModel.markAttendance(
                        dateIso = selectedDateStr,
                        status = status,
                        overtimeHours = otHours,
                        notes = notes
                    )
                    showEditSheet = false
                    showSuccessOverlay = true
                },
                onCancel = { showEditSheet = false }
            )
        }
    }

    SuccessFeedbackOverlay(
        visible = showSuccessOverlay,
        title = "Attendance Saved!",
        subtitle = "Aapki hajiri darj kar di gayi hai",
        onDismiss = { showSuccessOverlay = false }
    )
}

@Composable
fun FilterPill(title: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun CalendarDayCircle(
    day: Int,
    isCurrentMonth: Boolean,
    attendance: Attendance?,
    isSelected: Boolean,
    isToday: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val statusColor = when (attendance?.status) {
        AttendanceStatus.PRESENT -> EmeraldGreen
        AttendanceStatus.ABSENT -> DangerRed
        AttendanceStatus.HALF_DAY -> WarningOrange
        AttendanceStatus.WEEKLY_OFF -> RoyalBlue
        else -> null
    }

    val scaleAnim by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 650f),
        label = "calendarCircleScale"
    )

    Column(
        modifier = modifier
            .clip(CircleShape)
            .clickable(enabled = isCurrentMonth) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .scale(scaleAnim)
                .size(32.dp)
                .border(
                    width = if (isSelected || isToday) 2.dp else 0.dp,
                    color = if (isSelected) RoyalBlue else WarningOrange,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(
                    when {
                        statusColor != null -> statusColor
                        isSelected -> RoyalBlue.copy(alpha = 0.2f)
                        else -> Color.Transparent
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$day",
                fontSize = 13.sp,
                fontWeight = if (statusColor != null || isSelected) FontWeight.Bold else FontWeight.Medium,
                color = when {
                    statusColor != null -> Color.White
                    !isCurrentMonth -> TextSecondary.copy(alpha = 0.45f)
                    else -> TextPrimary
                }
            )
        }
    }
}

@Composable
fun SummaryStatBox(
    count: String,
    label: String,
    color: Color,
    bg: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .springPressEffect()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val countInt = count.toIntOrNull()
            if (countInt != null) {
                AnimatedCountText(
                    count = countInt,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = color
                )
            } else {
                Text(
                    text = count,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}
