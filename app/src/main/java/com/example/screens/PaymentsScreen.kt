package com.example.screens

import com.example.components.AnimatedRupeeText
import com.example.components.PremiumButton
import com.example.components.SuccessFeedbackOverlay
import com.example.components.shimmerShine
import com.example.components.springPressEffect
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Payment
import com.example.data.models.PaymentMethod
import com.example.data.models.PaymentType
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit = {}
) {
    val payments by viewModel.jobPaymentsList.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Salary, 1 = Payments
    var showAddDialog by remember { mutableStateOf(false) }
    var showSuccessOverlay by remember { mutableStateOf(false) }

    val hisab by viewModel.currentMonthHisab.collectAsState()
    val totalReceived = payments.sumOf { it.amount }
    val totalSalary = hisab?.totalEarned ?: 0.0
    val totalPending = (totalSalary - totalReceived).coerceAtLeast(0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Salary & Payments",
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Segmented Control Switcher matching reference Screen 7: [Salary] [Payments]
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFE8EEF8))
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == 0) RoyalBlue else Color.Transparent)
                                .clickable { selectedTab = 0 }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Salary",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 0) Color.White else TextSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == 1) RoyalBlue else Color.Transparent)
                                .clickable { selectedTab = 1 }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Payments",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 1) Color.White else TextSecondary
                            )
                        }
                    }
                }

                // 2. Total Salary Hero Card with Golden Money Bag matching reference Screen 7
                item {
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
                                            Color(0xFF1B55B3),
                                            Color(0xFF286DEC),
                                            Color(0xFF3880F5)
                                        )
                                    )
                                )
                                .shimmerShine(durationMillis = 3500)
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Total Salary",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    AnimatedRupeeText(
                                        amount = totalSalary,
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        ),
                                        color = Color.White
                                    )
                                }

                                // 3D Golden Money Bag Graphic
                                GoldenMoneyBagIllustration(sizeDp = 64)
                            }
                        }
                    }
                }

                // Sub-Row: 2 Clean Rounded Cards matching reference Screen 7
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldGreen)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Received",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = EmeraldGreen
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                AnimatedRupeeText(
                                    amount = totalReceived,
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = EmeraldGreen
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(DangerRed)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Pending",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = DangerRed
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                AnimatedRupeeText(
                                    amount = totalPending,
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = DangerRed
                                )
                            }
                        }
                    }
                }

                // 3. "Recent Payments" with "View All →"
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Payments",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "View All →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue
                        )
                    }
                }

                // Transaction Items List matching reference Screen 7:
                // ₹7,000 UPI • 10 Apr 2025
                // ₹5,000 Cash • 01 Apr 2025
                // ₹3,000 Bank Transfer • 25 Mar 2025
                items(payments) { payment ->
                    PaymentCardItem(payment = payment, onDelete = { viewModel.deletePayment(payment.id) })
                }
            }

            // Bottom "Add Payment" Button matching reference Screen 7 with shimmer and spring
            PremiumButton(
                onClick = { showAddDialog = true },
                text = "Add Payment",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                withShimmer = true
            )
        }
    }

    if (showAddDialog) {
        AddPaymentReferenceDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { amount, type, method, notes ->
                viewModel.addPayment(
                    amount = amount,
                    dateIso = "2025-04-16",
                    displayDate = "16 Apr 2025",
                    type = type,
                    method = method,
                    notes = notes
                )
                showAddDialog = false
                showSuccessOverlay = true
            }
        )
    }

    SuccessFeedbackOverlay(
        visible = showSuccessOverlay,
        title = "Payment Recorded!",
        subtitle = "Bhugtan safaltapoorvak jod diya gaya",
        onDismiss = { showSuccessOverlay = false }
    )
}

@Composable
fun PaymentCardItem(
    payment: Payment,
    onDelete: () -> Unit
) {
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
                // Soft Green Circle with Green Up Arrow ↑
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(SoftGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "₹${payment.amount.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${payment.paymentMethod.labelEn} • ${payment.displayDate}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = TextSecondary.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AddPaymentReferenceDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, PaymentType, PaymentMethod, String) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf(PaymentMethod.UPI) }
    var selectedType by remember { mutableStateOf(PaymentType.SALARY) }
    var notesText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Payment",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount (₹)") },
                    placeholder = { Text("e.g. 5000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(text = "Payment Method", fontSize = 12.sp, color = TextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(PaymentMethod.UPI, PaymentMethod.CASH, PaymentMethod.BANK_TRANSFER).forEach { method ->
                        val isSelected = selectedMethod == method
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) RoyalBlue else Color(0xFFF0F4FA))
                                .clickable { selectedMethod = method }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = method.labelEn,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextPrimary
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Note (Optional)") },
                    placeholder = { Text("e.g. Advance or Salary") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    if (amt > 0) {
                        onConfirm(amt, selectedType, selectedMethod, notesText)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun GoldenMoneyBagIllustration(modifier: Modifier = Modifier, sizeDp: Int = 64) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(Color(0xFFFFF0D0), Color(0xFFFFD56B).copy(alpha = 0.5f), Color.Transparent)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Tie rope at top
            Box(
                modifier = Modifier
                    .size((sizeDp * 0.22).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD97706))
            )
            // Golden bag pouch
            Box(
                modifier = Modifier
                    .size((sizeDp * 0.65).dp)
                    .clip(RoundedCornerShape((sizeDp * 0.32).dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFD56B), Color(0xFFF59E0B), Color(0xFFD97706))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "₹",
                    fontSize = (sizeDp * 0.32).sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF5A3902)
                )
            }
        }
    }
}

