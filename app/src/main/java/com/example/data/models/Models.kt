package com.example.data.models

enum class SalaryType(val labelEn: String, val labelHi: String) {
    DAILY("Daily", "दैनिक दिहाड़ी"),
    MONTHLY("Monthly", "मासिक वेतन"),
    HOURLY("Hourly", "घंटे के अनुसार"),
    CONTRACT("Contract", "ठेका / अनुबंध")
}

enum class AttendanceStatus(val labelEn: String, val labelHi: String, val colorHex: Long) {
    PRESENT("Present", "उपस्थित (P)", 0xFF16A878),       // Emerald Green
    ABSENT("Absent", "अनुपस्थित (A)", 0xFFEB4D4D),       // Danger Red
    HALF_DAY("Half Day", "आधा दिन (HD)", 0xFFF39C12),    // Warning Orange
    PAID_LEAVE("Paid Leave", "सवेतन छुट्टी (PL)", 0xFF8B5CF6), // Purple
    WEEKLY_OFF("Weekly Off", "साप्ताहिक अवकाश (WO)", 0xFF3867E8), // Royal Blue
    HOLIDAY("Holiday", "त्योहार / छुट्टी (H)", 0xFF64748B) // Slate
}

enum class PaymentType(val labelEn: String, val labelHi: String) {
    SALARY("Salary", "वेतन / सैलरी"),
    ADVANCE("Advance", "एडवांस"),
    BONUS("Bonus", "बोनस"),
    OVERTIME("Overtime", "ओवरटाइम"),
    SETTLEMENT("Settlement", "हिसाब चुकता"),
    OTHER("Other", "अन्य")
}

enum class PaymentMethod(val labelEn: String, val labelHi: String) {
    UPI("UPI", "UPI"),
    CASH("Cash", "नकद (Cash)"),
    BANK_TRANSFER("Bank Transfer", "बैंक ट्रांसफर"),
    CHEQUE("Cheque", "चेक")
}

data class Job(
    val id: String,
    val title: String,
    val employerName: String,
    val location: String = "Noida, UP",
    val salaryType: SalaryType = SalaryType.DAILY,
    val dailyRate: Double = 600.0,
    val halfDayRate: Double = 300.0,
    val monthlyRate: Double = 18000.0,
    val hourlyRate: Double = 75.0,
    val overtimeHourlyRate: Double = 100.0,
    val standardHoursPerDay: Int = 8,
    val monthlyWorkingDays: Int = 26,
    val joiningDate: String = "2025-01-01",
    val phone: String = "9876543210",
    val notes: String = ""
)

data class Attendance(
    val id: String,
    val jobId: String,
    val dateIso: String, // Format: YYYY-MM-DD
    val status: AttendanceStatus,
    val checkIn: String = "09:00 AM",
    val checkOut: String = "06:00 PM",
    val workingHours: String = "8h 0m",
    val overtimeHours: Double = 0.0,
    val earnings: Double = 600.0,
    val notes: String = "",
    val receiptUri: String? = null
)

data class Payment(
    val id: String,
    val jobId: String,
    val amount: Double,
    val dateIso: String,
    val displayDate: String = "10 Apr 2025",
    val paymentType: PaymentType = PaymentType.SALARY,
    val paymentMethod: PaymentMethod = PaymentMethod.UPI,
    val transactionRef: String = "",
    val notes: String = "",
    val receiptUri: String? = null,
    val allocationStatus: String = "ALLOCATED"
)

data class MonthlyHisab(
    val jobId: String,
    val year: Int,
    val month: Int, // 1 - 12
    val totalDaysInMonth: Int = 30,
    val presentDays: Int = 25,
    val absentDays: Int = 2,
    val halfDays: Int = 1,
    val paidLeaveDays: Int = 0,
    val weeklyOffDays: Int = 2,
    val overtimeHours: Double = 0.0,
    val basicEarned: Double = 15600.0,
    val bonus: Double = 1000.0,
    val deductions: Double = 1000.0,
    val overtimeEarned: Double = 0.0,
    val totalEarned: Double = 18500.0,
    val totalReceived: Double = 12000.0,
    val pendingAmount: Double = 6500.0,
    val isSettled: Boolean = false,
    val settlementStatus: String = "Settlement Pending"
)

data class UserProfile(
    val name: String = "Rohit Kumar",
    val email: String = "rohit@gmail.com",
    val phone: String = "+91 98765 43210",
    val preferredLanguage: String = "hi",
    val isPinEnabled: Boolean = false,
    val pinCode: String = "1234",
    val isBiometricEnabled: Boolean = false,
    val lastBackupTimestamp: Long = 0L,
    val autoBackupFrequency: String = "DAILY",
    val currencySymbol: String = "₹"
)

data class WorkNote(
    val id: String,
    val jobId: String,
    val dateIso: String,
    val title: String,
    val content: String,
    val category: String = "General",
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class PaymentReminder(
    val id: String,
    val jobId: String,
    val employerName: String,
    val expectedAmount: Double,
    val dueDateIso: String,
    val notes: String = "",
    val isResolved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class BackupHistory(
    val id: String,
    val timestamp: Long,
    val dateFormatted: String,
    val backupType: String,
    val recordCount: Int,
    val fileSizeFormatted: String,
    val status: String = "SUCCESS"
)

data class EmployerBalance(
    val employerName: String,
    val jobTitle: String,
    val totalEarned: Double,
    val totalReceived: Double,
    val pendingAmount: Double,
    val daysWorked: Int
)

data class FinancialTimelineItem(
    val id: String,
    val dateIso: String,
    val displayDate: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val isCredit: Boolean,
    val type: String,
    val statusColorHex: Long
)

data class DataAccuracyReport(
    val missingAttendanceCount: Int,
    val unsettledMonthsCount: Int,
    val unallocatedPaymentsCount: Int,
    val daysWithoutBackup: Int,
    val totalRecords: Int,
    val dbSizeBytes: Long,
    val integrityPassed: Boolean
)
