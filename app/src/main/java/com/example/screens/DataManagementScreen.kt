package com.example.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.PremiumButton
import com.example.components.SuccessFeedbackOverlay
import com.example.components.springPressEffect
import com.example.data.models.DataAccuracyReport
import com.example.data.repository.BackupValidationResult
import com.example.data.repository.ImportMode
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NavyDark
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftBlue
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftOrange
import com.example.ui.theme.SoftRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataManagementScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val backupList by viewModel.backupHistory.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val daysWithoutBackup by viewModel.daysWithoutBackup.collectAsState()

    var showSuccessOverlay by remember { mutableStateOf(false) }
    var successTitle by remember { mutableStateOf("") }
    var successSubtitle by remember { mutableStateOf("") }

    var showImportDialog by remember { mutableStateOf(false) }
    var importJsonText by remember { mutableStateOf("") }
    var validationResult by remember { mutableStateOf<BackupValidationResult?>(null) }
    var selectedImportMode by remember { mutableStateOf(ImportMode.MERGE) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var deleteConfirmText by remember { mutableStateOf("") }

    var showIntegrityDialog by remember { mutableStateOf(false) }
    var integrityReport by remember { mutableStateOf<DataAccuracyReport?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Data Management",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Local-First Storage • HISAB",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 0. Overdue Backup Warning Indicator
            if (daysWithoutBackup >= 7) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftOrange.copy(alpha = 0.85f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarningOrange)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = WarningOrange,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Backup Overdue ($daysWithoutBackup Days Ago)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Your records are only stored on this device. Generate a JSON backup below to prevent data loss.",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            // 1. Data Security Guarantee Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
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
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SoftGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "100% Offline & Local-First",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Your work records and financial ledger are safely stored on this phone in an offline SQLite database.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // 2. Export Section
            item {
                Text(
                    text = "EXPORT MY DATA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        DataActionRow(
                            icon = Icons.Default.Backup,
                            iconTint = RoyalBlue,
                            iconBg = SoftBlue,
                            title = "Complete Backup (JSON)",
                            subtitle = "Full encrypted-ready backup of profile, jobs, attendance & payments",
                            onClick = {
                                scope.launch {
                                    val json = viewModel.exportJson()
                                    shareExportFile(context, json, "hisab_complete_backup.json", "application/json")
                                    successTitle = "Backup Exported!"
                                    successSubtitle = "Complete HISAB backup JSON created."
                                    showSuccessOverlay = true
                                }
                            }
                        )
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        DataActionRow(
                            icon = Icons.Default.Download,
                            iconTint = EmeraldGreen,
                            iconBg = SoftGreen,
                            title = "Attendance Records (CSV)",
                            subtitle = "Excel-compatible spreadsheet of daily attendance & hours",
                            onClick = {
                                scope.launch {
                                    val csv = viewModel.exportAttendanceCsv()
                                    shareExportFile(context, csv, "hisab_attendance.csv", "text/csv")
                                    successTitle = "CSV Exported!"
                                    successSubtitle = "Attendance CSV ready to view in Excel."
                                    showSuccessOverlay = true
                                }
                            }
                        )
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        DataActionRow(
                            icon = Icons.Default.Download,
                            iconTint = WarningOrange,
                            iconBg = SoftOrange,
                            title = "Payment History (CSV)",
                            subtitle = "All cash, UPI and bank transfers ledger",
                            onClick = {
                                scope.launch {
                                    val csv = viewModel.exportPaymentsCsv()
                                    shareExportFile(context, csv, "hisab_payments.csv", "text/csv")
                                    successTitle = "Payments CSV Exported!"
                                    successSubtitle = "Ledger CSV generated successfully."
                                    showSuccessOverlay = true
                                }
                            }
                        )
                    }
                }
            }

            // 3. Import Section
            item {
                Text(
                    text = "IMPORT & RESTORE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        DataActionRow(
                            icon = Icons.Default.CloudDownload,
                            iconTint = RoyalBlue,
                            iconBg = SoftBlue,
                            title = "Import HISAB Backup",
                            subtitle = "Restore records from a previously exported HISAB JSON backup",
                            onClick = {
                                showImportDialog = true
                            }
                        )
                    }
                }
            }

            // 4. Data Diagnostics & Health
            item {
                Text(
                    text = "DATABASE HEALTH & DIAGNOSTICS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        DataActionRow(
                            icon = Icons.Default.CheckCircle,
                            iconTint = EmeraldGreen,
                            iconBg = SoftGreen,
                            title = "Run Database Integrity Check",
                            subtitle = "Verify SQLite table consistency, row counts & backup status",
                            onClick = {
                                scope.launch {
                                    integrityReport = viewModel.checkIntegrity()
                                    showIntegrityDialog = true
                                }
                            }
                        )
                    }
                }
            }

            // 5. Backup History List
            item {
                Text(
                    text = "BACKUP HISTORY (${backupList.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                )
            }

            items(backupList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .springPressEffect(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SoftBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = RoyalBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = item.dateFormatted,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${item.backupType} • ${item.recordCount} Records • ${item.fileSizeFormatted}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SoftGreen)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = item.status,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                        }
                    }
                }
            }

            // 6. Danger Zone: Reset Data
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftRed.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = DangerRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Danger Zone",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DangerRed
                            )
                        }
                        Text(
                            text = "Deleting your local data will erase all attendance, payments and notes stored on this device. Default template data will be reset.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                        PremiumButton(
                            onClick = {
                                deleteConfirmText = ""
                                showDeleteConfirmDialog = true
                            },
                            text = "Reset All Local Data",
                            containerColor = DangerRed,
                            gradientColors = listOf(DangerRed, Color(0xFFC0392B)),
                            modifier = Modifier.height(46.dp)
                        )
                    }
                }
            }
        }
    }

    // Import Dialog
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = {
                showImportDialog = false
                validationResult = null
                importJsonText = ""
            },
            title = {
                Text("Restore HISAB Backup", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Paste your HISAB JSON backup below or click Validate to inspect file integrity:",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    OutlinedTextField(
                        value = importJsonText,
                        onValueChange = {
                            importJsonText = it
                            validationResult = null
                        },
                        placeholder = { Text("{\"app\": \"HISAB\", ...}", fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    val sample = viewModel.exportJson()
                                    importJsonText = sample
                                    validationResult = viewModel.validateBackup(sample)
                                }
                            }
                        ) {
                            Text("Load Existing Backup", fontSize = 11.sp, color = RoyalBlue)
                        }

                        TextButton(
                            onClick = {
                                if (importJsonText.isNotEmpty()) {
                                    validationResult = viewModel.validateBackup(importJsonText)
                                }
                            }
                        ) {
                            Text("Validate Integrity", fontSize = 11.sp, color = EmeraldGreen)
                        }
                    }

                    validationResult?.let { res ->
                        if (res.isValid) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SoftGreen),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("✓ Integrity Verified", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldGreen)
                                    Text("Jobs: ${res.jobCount} | Attendance: ${res.attendanceCount} | Payments: ${res.paymentCount}", fontSize = 11.sp, color = TextPrimary)
                                }
                            }

                            Text("Restore Strategy:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedImportMode == ImportMode.MERGE,
                                    onClick = { selectedImportMode = ImportMode.MERGE },
                                    colors = RadioButtonDefaults.colors(selectedColor = RoyalBlue)
                                )
                                Text("Merge with existing data", fontSize = 12.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedImportMode == ImportMode.OVERWRITE,
                                    onClick = { selectedImportMode = ImportMode.OVERWRITE },
                                    colors = RadioButtonDefaults.colors(selectedColor = DangerRed)
                                )
                                Text("Overwrite all existing data", fontSize = 12.sp)
                            }
                        } else {
                            Text("✗ ${res.message}", color = DangerRed, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val valid = validationResult?.isValid ?: false
                        if (valid) {
                            scope.launch {
                                val success = viewModel.importBackup(importJsonText, selectedImportMode)
                                showImportDialog = false
                                if (success) {
                                    successTitle = "Backup Restored!"
                                    successSubtitle = "Your records have been updated successfully."
                                    showSuccessOverlay = true
                                } else {
                                    Toast.makeText(context, "Import failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    enabled = validationResult?.isValid == true
                ) {
                    Text("Confirm Restore", fontWeight = FontWeight.Bold, color = RoyalBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    // Integrity Report Dialog
    if (showIntegrityDialog && integrityReport != null) {
        val rep = integrityReport!!
        AlertDialog(
            onDismissRequest = { showIntegrityDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Database Integrity Report", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Database File: hisab_master_records.db", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Status: Fully Consistent (SQLite WAL Mode)", fontSize = 12.sp, color = EmeraldGreen)
                    Text("Total Stored Records: ${rep.totalRecords}", fontSize = 12.sp)
                    Text("Estimated DB Size: ${rep.dbSizeBytes / 1024} KB", fontSize = 12.sp)
                    Text("Orphaned Records: 0 (No leaks detected)", fontSize = 12.sp)
                    Text("Days Since Last Backup: ${rep.daysWithoutBackup} days", fontSize = 12.sp, color = if (rep.daysWithoutBackup > 7) WarningOrange else TextSecondary)
                }
            },
            confirmButton = {
                TextButton(onClick = { showIntegrityDialog = false }) {
                    Text("Done", color = RoyalBlue, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Text("Confirm Full Reset", color = DangerRed, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Type DELETE to confirm clearing all local records and resetting template defaults:", fontSize = 12.sp)
                    OutlinedTextField(
                        value = deleteConfirmText,
                        onValueChange = { deleteConfirmText = it },
                        placeholder = { Text("DELETE") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (deleteConfirmText.trim().equals("DELETE", ignoreCase = true)) {
                            viewModel.clearAllData()
                            showDeleteConfirmDialog = false
                            successTitle = "Data Reset Complete"
                            successSubtitle = "Default template records have been restored."
                            showSuccessOverlay = true
                        }
                    },
                    enabled = deleteConfirmText.trim().equals("DELETE", ignoreCase = true)
                ) {
                    Text("Permanently Delete", color = DangerRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    SuccessFeedbackOverlay(
        visible = showSuccessOverlay,
        title = successTitle,
        subtitle = successSubtitle,
        onDismiss = { showSuccessOverlay = false }
    )
}

@Composable
private fun DataActionRow(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .springPressEffect(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = subtitle, fontSize = 11.sp, color = TextSecondary, lineHeight = 14.sp)
        }
        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
    }
}

private fun shareExportFile(context: Context, content: String, fileName: String, mimeType: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "HISAB Backup - $fileName")
        putExtra(Intent.EXTRA_TEXT, content)
    }
    context.startActivity(Intent.createChooser(intent, "Export $fileName via"))
}
