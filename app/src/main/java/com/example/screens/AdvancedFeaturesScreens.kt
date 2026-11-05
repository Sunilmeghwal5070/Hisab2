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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.AnimatedRupeeText
import com.example.components.PremiumButton
import com.example.components.SuccessFeedbackOverlay
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
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel
import java.time.LocalDate

// ----------------------------------------------------
// A. Smart Financial Timeline Screen
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialTimelineScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit
) {
    val timelineItems by viewModel.financialTimeline.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Financial Timeline", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "CHRONOLOGICAL LEDGER (${timelineItems.size} Entries)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            items(timelineItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .springPressEffect(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (item.isCredit) SoftGreen else SoftBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (item.isCredit) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = if (item.isCredit) EmeraldGreen else RoyalBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${item.displayDate} • ${item.subtitle}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Text(
                            text = "${if (item.isCredit) "+" else ""}₹${item.amount.toInt()}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (item.isCredit) EmeraldGreen else RoyalBlue
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// B. Employer Balance Sheet Screen
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerBalanceScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit
) {
    val balances by viewModel.employerBalances.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Employer Balance Sheet", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "EMPLOYER / CONTRACTOR BALANCES",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            items(balances) { bal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .springPressEffect(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = bal.employerName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = bal.jobTitle,
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SoftBlue)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${bal.daysWorked} Days Worked",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Billed", fontSize = 11.sp, color = TextSecondary)
                                Text("₹${bal.totalEarned.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            Column {
                                Text("Total Received", fontSize = 11.sp, color = TextSecondary)
                                Text("₹${bal.totalReceived.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Net Pending", fontSize = 11.sp, color = DangerRed)
                                Text("₹${bal.pendingAmount.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = DangerRed)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// C. Payment Reminders Screen
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentRemindersScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit
) {
    val reminders by viewModel.paymentReminders.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var empName by remember { mutableStateOf("") }
    var expAmount by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("2025-04-30") }
    var note by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Payment Reminders", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = RoyalBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Reminder")
            }
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "EXPECTED & PENDING REMINDERS (${reminders.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            items(reminders) { rem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .springPressEffect(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (rem.isResolved) SoftGreen else SoftOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (rem.isResolved) Icons.Default.CheckCircle else Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = if (rem.isResolved) EmeraldGreen else WarningOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = rem.employerName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Due: ${rem.dueDateIso} • ${rem.notes}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${rem.expectedAmount.toInt()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (rem.isResolved) EmeraldGreen else WarningOrange
                            )
                            if (!rem.isResolved) {
                                Text(
                                    text = "Mark Paid ✓",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue,
                                    modifier = Modifier
                                        .clickable { viewModel.resolveReminder(rem.id) }
                                        .padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Payment Reminder", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = empName,
                        onValueChange = { empName = it },
                        label = { Text("Employer / Client Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = expAmount,
                        onValueChange = { expAmount = it },
                        label = { Text("Expected Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = dueDate,
                        onValueChange = { dueDate = it },
                        label = { Text("Expected Date (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Notes / Purpose") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amt = expAmount.toDoubleOrNull() ?: 0.0
                        if (empName.isNotEmpty() && amt > 0) {
                            viewModel.addPaymentReminder(empName, amt, dueDate, note)
                            showAddDialog = false
                            empName = ""
                            expAmount = ""
                        }
                    }
                ) {
                    Text("Save Reminder", color = RoyalBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

// ----------------------------------------------------
// D. Work Notes & Diary Screen
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkNotesScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit
) {
    val notes by viewModel.workNotes.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Site Work") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Work Diary & Notes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = RoyalBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "DAILY WORK NOTES & DIARY ENTRIES (${notes.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            if (notes.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.EventNote, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("No work notes added yet", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                            Text("Record materials received, site progress or dispute details.", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }

            items(notes) { noteItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .springPressEffect(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SoftBlue)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(noteItem.category, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RoyalBlue)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(noteItem.dateIso, fontSize = 12.sp, color = TextSecondary)
                            }
                            IconButton(
                                onClick = { viewModel.deleteWorkNote(noteItem.id) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", tint = DangerRed, modifier = Modifier.size(18.dp))
                            }
                        }
                        Text(noteItem.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                        Text(noteItem.content, fontSize = 13.sp, color = TextSecondary, lineHeight = 17.sp)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("New Work Note", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Note Title (e.g. Concrete mix delivered)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Description / Proof details") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Site Work", "Material", "Dispute").forEach { cat ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (category == cat) RoyalBlue else SoftBlue)
                                    .clickable { category = cat }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(cat, fontSize = 11.sp, color = if (category == cat) Color.White else RoyalBlue, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (title.isNotEmpty()) {
                            viewModel.addWorkNote(
                                dateIso = LocalDate.now().toString(),
                                title = title,
                                content = content,
                                category = category
                            )
                            showAddDialog = false
                            title = ""
                            content = ""
                        }
                    }
                ) {
                    Text("Save Note", color = RoyalBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}
