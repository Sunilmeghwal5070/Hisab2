package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AttendanceStatus
import com.example.data.models.Job
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftBlue
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftOrange
import com.example.ui.theme.SoftRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange

@Composable
fun QuickAttendanceSheetContent(
    selectedJob: Job?,
    todayStr: String,
    onSave: (AttendanceStatus, Double, String) -> Unit,
    onCancel: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf(AttendanceStatus.PRESENT) }
    var overtimeHours by remember { mutableDoubleStateOf(0.0) }
    var remarks by remember { mutableStateOf("") }

    val dailyRate = selectedJob?.dailyRate ?: 600.0
    val halfDayRate = selectedJob?.halfDayRate ?: 300.0
    val otRate = selectedJob?.overtimeHourlyRate ?: 100.0

    val previewEarnings = when (selectedStatus) {
        AttendanceStatus.PRESENT -> dailyRate + (overtimeHours * otRate)
        AttendanceStatus.HALF_DAY -> halfDayRate + (overtimeHours * otRate)
        AttendanceStatus.PAID_LEAVE -> dailyRate
        else -> 0.0
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Mark Attendance",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "$todayStr • ${selectedJob?.title ?: "Site Engineer - ABC Infra"}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Text(
                text = "₹${previewEarnings.toInt()}",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = EmeraldGreen
            )
        }

        // Attendance Status Options
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Select Status:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusSelectChipItem(
                    title = "Present",
                    selected = selectedStatus == AttendanceStatus.PRESENT,
                    activeColor = EmeraldGreen,
                    bg = SoftGreen,
                    onClick = { selectedStatus = AttendanceStatus.PRESENT },
                    modifier = Modifier.weight(1f)
                )
                StatusSelectChipItem(
                    title = "Half Day",
                    selected = selectedStatus == AttendanceStatus.HALF_DAY,
                    activeColor = WarningOrange,
                    bg = SoftOrange,
                    onClick = { selectedStatus = AttendanceStatus.HALF_DAY },
                    modifier = Modifier.weight(1f)
                )
                StatusSelectChipItem(
                    title = "Absent",
                    selected = selectedStatus == AttendanceStatus.ABSENT,
                    activeColor = MaterialTheme.colorScheme.error,
                    bg = SoftRed,
                    onClick = { selectedStatus = AttendanceStatus.ABSENT },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusSelectChipItem(
                    title = "Weekly Off",
                    selected = selectedStatus == AttendanceStatus.WEEKLY_OFF,
                    activeColor = RoyalBlue,
                    bg = SoftBlue,
                    onClick = { selectedStatus = AttendanceStatus.WEEKLY_OFF },
                    modifier = Modifier.weight(1f)
                )
                StatusSelectChipItem(
                    title = "Paid Leave",
                    selected = selectedStatus == AttendanceStatus.PAID_LEAVE,
                    activeColor = Color(0xFF8B5CF6),
                    bg = Color(0xFFF4EEFF),
                    onClick = { selectedStatus = AttendanceStatus.PAID_LEAVE },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Overtime Stepper
        if (selectedStatus == AttendanceStatus.PRESENT || selectedStatus == AttendanceStatus.HALF_DAY) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F7FC))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Overtime Hours",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "@ ₹${otRate.toInt()}/hour",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { if (overtimeHours > 0) overtimeHours -= 1.0 },
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "${overtimeHours.toInt()}h",
                            modifier = Modifier.padding(horizontal = 12.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                        OutlinedButton(
                            onClick = { overtimeHours += 1.0 },
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Save Button with shimmer and spring
        PremiumButton(
            onClick = {
                try {
                    val toneGen = android.media.ToneGenerator(android.media.AudioManager.STREAM_NOTIFICATION, 100)
                    toneGen.startTone(android.media.ToneGenerator.TONE_PROP_ACK, 200)
                } catch (e: Exception) {
                    // Ignore
                }
                onSave(selectedStatus, overtimeHours, remarks)
            },
            text = "Save Attendance",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            withShimmer = true
        )
    }
}

@Composable
fun StatusSelectChipItem(
    title: String,
    selected: Boolean,
    activeColor: Color,
    bg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .springPressEffect(pressedScale = 0.94f) { onClick() }
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) activeColor else bg)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (selected) Color.White else TextPrimary,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
