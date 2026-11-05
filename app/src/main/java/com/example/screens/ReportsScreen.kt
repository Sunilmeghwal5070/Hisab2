package com.example.screens

import android.content.Context
import android.content.Intent
import com.example.components.AnimatedCountText
import com.example.components.AnimatedRupeeText
import com.example.components.PremiumButton
import com.example.components.SuccessFeedbackOverlay
import com.example.components.shimmerShine
import com.example.components.springPressEffect
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.MonthlyHisab
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NavyDark
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftOrange
import com.example.ui.theme.SoftRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit = {}
) {
    val hisab by viewModel.currentMonthHisab.collectAsState()
    val selectedJob by viewModel.selectedJob.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current

    var settlementSaved by remember { mutableStateOf(false) }
    var showSuccessOverlay by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Monthly Hisab",
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
                    IconButton(onClick = {
                        shareHisabReport(context, userProfile.name, selectedJob?.title ?: "Site Engineer", hisab)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = RoyalBlue
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Month Selector Dropdown Pill: [Apr 2025 ∨]
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Card(
                            modifier = Modifier.width(180.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Apr 2025",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                        }
                    }
                }

                // 2. 3 Stat Cards in a row (Present, Absent, Half Day)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HisabStatCard(
                            count = (hisab?.presentDays ?: 0).toString(),
                            label = "Present",
                            color = EmeraldGreen,
                            bg = SoftGreen,
                            modifier = Modifier.weight(1f)
                        )
                        HisabStatCard(
                            count = (hisab?.absentDays ?: 0).toString(),
                            label = "Absent",
                            color = DangerRed,
                            bg = SoftRed,
                            modifier = Modifier.weight(1f)
                        )
                        HisabStatCard(
                            count = (hisab?.halfDays ?: 0).toString(),
                            label = "Half Day",
                            color = WarningOrange,
                            bg = SoftOrange,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 3. Financial Summary with colored bullet points
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Total Earned
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EmeraldGreen))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Total Earned",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextSecondary
                                    )
                                }
                                AnimatedRupeeText(
                                    amount = hisab?.totalEarned ?: 0.0,
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = EmeraldGreen
                                )
                            }

                            // Total Received
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(TextPrimary))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Total Received",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextSecondary
                                    )
                                }
                                AnimatedRupeeText(
                                    amount = hisab?.totalReceived ?: 0.0,
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = TextPrimary
                                )
                            }

                            // Remaining
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(DangerRed))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Remaining",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextSecondary
                                    )
                                }
                                AnimatedRupeeText(
                                    amount = hisab?.pendingAmount ?: 0.0,
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    ),
                                    color = DangerRed
                                )
                            }
                        }
                    }
                }

                // 4. Salary Breakdown section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Salary Breakdown",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Basic Earned", fontSize = 13.sp, color = TextSecondary)
                                Text(text = "₹${hisab?.basicEarned?.toInt() ?: 0}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Bonus", fontSize = 13.sp, color = TextSecondary)
                                Text(text = "₹${hisab?.bonus?.toInt() ?: 0}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Deductions", fontSize = 13.sp, color = TextSecondary)
                                Text(text = "₹${hisab?.deductions?.toInt() ?: 0}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                    }
                }

                // 5. Settlement Pending Badge
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(SoftGreen)
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = if (settlementSaved) "Settlement Saved ✓" else (hisab?.settlementStatus ?: "Settlement Pending"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                        }
                    }
                }
            }

            // 6. Save Settlement Button matching reference Screen 8 with shimmer and spring
            PremiumButton(
                onClick = {
                    settlementSaved = true
                    showSuccessOverlay = true
                },
                text = if (settlementSaved) "Settlement Saved ✓" else "Save Settlement",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                containerColor = NavyDark,
                gradientColors = listOf(Color(0xFF1E3A60), NavyDark),
                withShimmer = true
            )
        }
    }

    SuccessFeedbackOverlay(
        visible = showSuccessOverlay,
        title = "Settlement Saved!",
        subtitle = "Mahine ka hisab safaltapoorvak save ho gaya",
        onDismiss = { showSuccessOverlay = false }
    )
}

@Composable
fun HisabStatCard(
    count: String,
    label: String,
    color: Color,
    bg: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .springPressEffect()
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            val countInt = count.toIntOrNull()
            if (countInt != null) {
                AnimatedCountText(
                    count = countInt,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = color
                )
            } else {
                Text(
                    text = count,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }
        }
    }
}

private fun shareHisabReport(context: Context, userName: String, jobTitle: String, hisab: MonthlyHisab?) {
    val present = hisab?.presentDays ?: 0
    val absent = hisab?.absentDays ?: 0
    val halfDay = hisab?.halfDays ?: 0
    val earned = hisab?.totalEarned?.toInt() ?: 0
    val received = hisab?.totalReceived?.toInt() ?: 0
    val pending = hisab?.pendingAmount?.toInt() ?: 0
    val basic = hisab?.basicEarned?.toInt() ?: 0
    val bonus = hisab?.bonus?.toInt() ?: 0
    val deductions = hisab?.deductions?.toInt() ?: 0
    val status = hisab?.settlementStatus ?: "Pending"

    val text = """
📋 *HISAB – Monthly Work & Money Statement*
Worker: $userName
Job: $jobTitle
━━━━━━━━━━━━━━━━━━━━
🟢 Present: $present Days
🔴 Absent: $absent Days
🟠 Half Day: $halfDay Days
━━━━━━━━━━━━━━━━━━━━
💰 Total Earned: ₹$earned
💵 Total Received: ₹$received
👉 Remaining Pending: ₹$pending
━━━━━━━━━━━━━━━━━━━━
Basic: ₹$basic | Bonus: ₹$bonus | Deductions: ₹$deductions
Status: $status
(Generated securely by HISAB - Local-First Storage)
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "HISAB Report - $userName")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share HISAB Report"))
}
