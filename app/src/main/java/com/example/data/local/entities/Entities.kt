package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "primary_profile",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val preferredLanguage: String = "hi",
    val isPinEnabled: Boolean = false,
    val pinCode: String = "1234",
    val isBiometricEnabled: Boolean = false,
    val lastBackupTimestamp: Long = 0L,
    val autoBackupFrequency: String = "DAILY", // OFF, DAILY, WEEKLY, MONTHLY
    val currencySymbol: String = "₹"
)

@Entity(
    tableName = "jobs",
    indices = [Index(value = ["title", "employerName"])]
)
data class JobEntity(
    @PrimaryKey val id: String,
    val title: String,
    val employerName: String,
    val location: String = "Noida, UP",
    val salaryType: String = "DAILY", // DAILY, MONTHLY, HOURLY
    val dailyRate: Double = 600.0,
    val halfDayRate: Double = 300.0,
    val monthlyRate: Double = 18000.0,
    val hourlyRate: Double = 75.0,
    val overtimeHourlyRate: Double = 100.0,
    val sundayRate: Double = 600.0,
    val phone: String = "",
    val notes: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "attendance",
    primaryKeys = ["dateIso", "jobId"],
    indices = [Index(value = ["jobId"]), Index(value = ["dateIso"])]
)
data class AttendanceEntity(
    val dateIso: String,
    val jobId: String,
    val status: String = "PRESENT", // PRESENT, ABSENT, HALF_DAY, WEEKLY_OFF, PAID_LEAVE
    val workingHours: String = "8h 0m",
    val overtimeHours: Double = 0.0,
    val earnings: Double = 600.0,
    val notes: String = "",
    val receiptUri: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "payments",
    indices = [Index(value = ["jobId"]), Index(value = ["dateIso"])]
)
data class PaymentEntity(
    @PrimaryKey val id: String,
    val jobId: String,
    val amount: Double,
    val dateIso: String,
    val displayDate: String,
    val paymentType: String = "SALARY", // SALARY, ADVANCE, BONUS, OVERTIME, SETTLEMENT
    val paymentMethod: String = "UPI", // UPI, CASH, BANK_TRANSFER, CHEQUE
    val transactionRef: String = "",
    val notes: String = "",
    val receiptUri: String? = null,
    val allocationStatus: String = "ALLOCATED", // ALLOCATED, PENDING, DISPUTED
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "settlements",
    indices = [Index(value = ["jobId", "year", "month"], unique = true)]
)
data class SettlementEntity(
    @PrimaryKey val id: String,
    val jobId: String,
    val year: Int,
    val month: Int,
    val totalEarned: Double = 15600.0,
    val totalReceived: Double = 12000.0,
    val pendingAmount: Double = 3600.0,
    val basicEarned: Double = 15600.0,
    val bonus: Double = 1000.0,
    val deductions: Double = 1000.0,
    val isSettled: Boolean = false,
    val settlementStatus: String = "Settlement Pending",
    val settledDateIso: String? = null,
    val notes: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "work_notes",
    indices = [Index(value = ["jobId"]), Index(value = ["dateIso"])]
)
data class WorkNoteEntity(
    @PrimaryKey val id: String,
    val jobId: String,
    val dateIso: String,
    val title: String,
    val content: String,
    val category: String = "General", // Site Work, Material, Overtime, Dispute, Diary
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "payment_reminders",
    indices = [Index(value = ["jobId"]), Index(value = ["dueDateIso"])]
)
data class PaymentReminderEntity(
    @PrimaryKey val id: String,
    val jobId: String,
    val employerName: String,
    val expectedAmount: Double,
    val dueDateIso: String,
    val notes: String = "",
    val isResolved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "backup_history")
data class BackupHistoryEntity(
    @PrimaryKey val id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val dateFormatted: String,
    val backupType: String = "MANUAL", // MANUAL, AUTO, EXPORT_JSON
    val recordCount: Int,
    val fileSizeFormatted: String,
    val status: String = "SUCCESS"
)
