package com.example.screens

import com.example.components.shimmerShine
import com.example.components.springPressEffect
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.SalaryType
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CyanTeal
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NavyDark
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import com.example.viewmodels.KaamKhataViewModel

@Composable
fun OnboardingScreen(
    viewModel: KaamKhataViewModel,
    onOnboardingComplete: () -> Unit
) {
    var currentSlide by remember { mutableIntStateOf(0) }

    // Form fields for registration (Slide 2)
    var fullName by remember { mutableStateOf("") }
    var emailPhone by remember { mutableStateOf("") }
    var jobName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var workLocation by remember { mutableStateOf("") }
    var salaryType by remember { mutableStateOf(SalaryType.DAILY) }
    var perDayAmount by remember { mutableStateOf("") }
    var halfDayAmount by remember { mutableStateOf("") }

    var formError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = BackgroundLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE4F0FF),
                            Color(0xFFF3F7FD),
                            Color(0xFFFFFFFF)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with "Skip" button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (currentSlide < 2) {
                    Text(
                        text = "Skip",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RoyalBlue,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { currentSlide = 2 }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Slide Content
            AnimatedContent(
                targetState = currentSlide,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                    } else {
                        (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = "onboardingSlide"
            ) { slide ->
                when (slide) {
                    0 -> OnboardingSlide1()
                    1 -> OnboardingSlide2()
                    else -> OnboardingSetupForm(
                        fullName = fullName,
                        onFullNameChange = { fullName = it; formError = null },
                        jobName = jobName,
                        onJobNameChange = { jobName = it; formError = null },
                        companyName = companyName,
                        onCompanyNameChange = { companyName = it },
                        workLocation = workLocation,
                        onWorkLocationChange = { workLocation = it },
                        salaryType = salaryType,
                        onSalaryTypeChange = { salaryType = it },
                        perDayAmount = perDayAmount,
                        onPerDayAmountChange = { perDayAmount = it },
                        halfDayAmount = halfDayAmount,
                        onHalfDayAmountChange = { halfDayAmount = it },
                        errorMessage = formError
                    )
                }
            }

            // Bottom Section: Indicator Dots & Next / Finish Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 4 Progress Dots matching reference image
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        val isSelected = index == currentSlide
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (isSelected) 24.dp else 6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) RoyalBlue else RoyalBlue.copy(alpha = 0.25f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Royal Blue Pill Button
                Button(
                    onClick = {
                        if (currentSlide < 2) {
                            currentSlide += 1
                        } else {
                            // Validation: User must fill their details!
                            val trimmedName = fullName.trim()
                            val trimmedJob = jobName.trim()
                            val wage = perDayAmount.toDoubleOrNull() ?: 0.0

                            when {
                                trimmedName.isEmpty() -> formError = "Please enter your name."
                                trimmedJob.isEmpty() -> formError = "Please enter your job name."
                                wage <= 0 -> formError = "Please enter a valid wage amount."
                                else -> {
                                    viewModel.completeOnboarding(
                                        userName = trimmedName,
                                        email = emailPhone.trim().ifEmpty { "rohit@gmail.com" },
                                        phone = "+91 98765 43210",
                                        jobTitle = "$trimmedJob - ${companyName.trim().ifEmpty { "ABC Infra" }}",
                                        employerName = companyName.trim().ifEmpty { "ABC Infra Pvt. Ltd." },
                                        location = workLocation.trim().ifEmpty { "Noida, UP" },
                                        wage = wage,
                                        salaryType = salaryType
                                    )
                                    onOnboardingComplete()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .springPressEffect()
                        .shadow(8.dp, RoundedCornerShape(27.dp), spotColor = RoyalBlue)
                        .shimmerShine(durationMillis = 3200),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
                ) {
                    Text(
                        text = if (currentSlide < 2) "Next →" else "Get Started →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingSlide1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 3D-styled Calendar & Clock Illustration matching reference Screen 2
        Box(
            modifier = Modifier.size(220.dp),
            contentAlignment = Alignment.Center
        ) {
            // Circular soft blue pedestal
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFD6E7FE), Color(0xFFE8F2FE), Color.Transparent)
                        )
                    )
            )

            // 3D Blue & White Calendar
            Card(
                modifier = Modifier
                    .size(130.dp)
                    .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = RoyalBlue),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(RoyalBlue, Color(0xFF1E5CCB))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White))
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White))
                        }
                    }

                    // Green checkmark in calendar
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F8F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            // Blue Clock badge on right
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(54.dp)
                    .shadow(10.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = RoyalBlue,
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Manage Daily\nAttendance",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Mark your attendance\nwith just one tap.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun OnboardingSlide2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 3D-styled Golden Money Sack & Coins matching reference Screen 3
        Box(
            modifier = Modifier.size(220.dp),
            contentAlignment = Alignment.Center
        ) {
            // Soft circular pedestal
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFFEF0D4), Color(0xFFFFF7E8), Color.Transparent)
                        )
                    )
            )

            // Golden Sack with Rupee symbol
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top tie rope of sack
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE6A329))
                )

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(16.dp, RoundedCornerShape(55.dp), spotColor = WarningOrange)
                        .clip(RoundedCornerShape(55.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFFFC542), Color(0xFFF39C12), Color(0xFFD68007))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "₹",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF5A3902)
                    )
                }

                // Coins around sack
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD56B))
                                .shadow(2.dp, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("₹", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5A3902))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Track Salary\nAutomatically",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Know your earnings\nand pending amount.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun OnboardingSetupForm(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    jobName: String,
    onJobNameChange: (String) -> Unit,
    companyName: String,
    onCompanyNameChange: (String) -> Unit,
    workLocation: String,
    onWorkLocationChange: (String) -> Unit,
    salaryType: SalaryType,
    onSalaryTypeChange: (SalaryType) -> Unit,
    perDayAmount: String,
    onPerDayAmountChange: (String) -> Unit,
    halfDayAmount: String,
    onHalfDayAmountChange: (String) -> Unit,
    errorMessage: String?
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Briefcase Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F2FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Setup Your Profile & Job",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Enter details to create your digital register.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        if (errorMessage != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFDEEEC))
                ) {
                    Text(
                        text = "⚠️ $errorMessage",
                        color = Color(0xFFD32F2F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                label = { Text("Your Name *") },
                placeholder = { Text("Rohit Kumar") },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 15.sp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = RoyalBlue,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = RoyalBlue,
                    unfocusedLabelColor = TextSecondary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = jobName,
                onValueChange = onJobNameChange,
                label = { Text("Job Name *") },
                placeholder = { Text("Site Engineer") },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 15.sp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = RoyalBlue,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = RoyalBlue,
                    unfocusedLabelColor = TextSecondary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = companyName,
                onValueChange = onCompanyNameChange,
                label = { Text("Company Name") },
                placeholder = { Text("ABC Infra Pvt. Ltd.") },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 15.sp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = RoyalBlue,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = RoyalBlue,
                    unfocusedLabelColor = TextSecondary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = workLocation,
                onValueChange = onWorkLocationChange,
                label = { Text("Work Location") },
                placeholder = { Text("Noida, UP") },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 15.sp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = RoyalBlue,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = RoyalBlue,
                    unfocusedLabelColor = TextSecondary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Text(
                text = "Salary Type",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(SalaryType.DAILY, SalaryType.MONTHLY, SalaryType.HOURLY).forEach { type ->
                    val isSelected = salaryType == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) RoyalBlue else Color.White)
                            .clickable { onSalaryTypeChange(type) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = type.labelEn,
                            color = if (isSelected) Color.White else TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = perDayAmount,
                    onValueChange = onPerDayAmountChange,
                    label = { Text("Per Day Amount") },
                    prefix = { Text("₹ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 15.sp),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = RoyalBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = RoyalBlue,
                        unfocusedLabelColor = TextSecondary
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = halfDayAmount,
                    onValueChange = onHalfDayAmountChange,
                    label = { Text("Half Day Amount") },
                    prefix = { Text("₹ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 15.sp),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = RoyalBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = RoyalBlue,
                        unfocusedLabelColor = TextSecondary
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}
