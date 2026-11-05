package com.example.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainActivity
import com.example.components.PremiumButton
import com.example.components.SuccessFeedbackOverlay
import com.example.receivers.ReminderActionReceiver
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftBlue
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRemindersSettingsScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var remindersEnabled by remember { mutableStateOf(true) }
    var startTime by remember { mutableStateOf("09:00 AM") }
    var endTime by remember { mutableStateOf("09:00 PM") }
    var intervalMinutes by remember { mutableStateOf("30 mins") }
    var showTestSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Attendance Reminders", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Enable / Disable Toggle Card
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(SoftBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = RoyalBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Smart Reminders",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Remind every 30 mins until attendance is saved",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Switch(
                            checked = remindersEnabled,
                            onCheckedChange = { remindersEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = RoyalBlue)
                        )
                    }
                }
            }

            // 2. Schedule Details Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Schedule Configuration",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        ReminderScheduleRow(label = "Start Time", value = startTime)
                        HorizontalDivider(color = Color(0xFFF0F4FA))
                        ReminderScheduleRow(label = "End Time", value = endTime)
                        HorizontalDivider(color = Color(0xFFF0F4FA))
                        ReminderScheduleRow(label = "Reminder Interval", value = intervalMinutes)
                        HorizontalDivider(color = Color(0xFFF0F4FA))
                        ReminderScheduleRow(label = "Working Days", value = "Mon - Sat (Sun Holiday)")
                    }
                }
            }

            // 3. Test Notification Button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                PremiumButton(
                    onClick = {
                        triggerTestNotification(context)
                        showTestSuccess = true
                    },
                    text = "Test Notification Reminder",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    withShimmer = true
                )
            }
        }
    }

    SuccessFeedbackOverlay(
        visible = showTestSuccess,
        title = "Test Reminder Sent!",
        subtitle = "Check your notification panel",
        onDismiss = { showTestSuccess = false }
    )
}

@Composable
fun ReminderScheduleRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}

fun triggerTestNotification(context: Context) {
    val channelId = "hisab_attendance_reminders"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, "Attendance Reminders", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Reminds you to mark your daily attendance"
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    val presentIntent = Intent(context, ReminderActionReceiver::class.java).apply {
        action = "ACTION_MARK_PRESENT"
    }
    val presentPi = PendingIntent.getBroadcast(context, 201, presentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val absentIntent = Intent(context, ReminderActionReceiver::class.java).apply {
        action = "ACTION_MARK_ABSENT"
    }
    val absentPi = PendingIntent.getBroadcast(context, 202, absentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val openIntent = Intent(context, MainActivity::class.java)
    val openPi = PendingIntent.getActivity(context, 203, openIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_menu_today)
        .setContentTitle("Don't forget today's attendance!")
        .setContentText("Your attendance for today has not been recorded. Open HISAB and update your work status.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(openPi)
        .addAction(android.R.drawable.ic_menu_upload, "Mark Present", presentPi)
        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Mark Absent", absentPi)
        .build()

    notificationManager.notify(3001, notification)
}
