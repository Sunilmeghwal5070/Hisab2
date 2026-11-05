package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.AttendanceEntity
import com.example.data.local.entities.BackupHistoryEntity
import com.example.data.local.entities.JobEntity
import com.example.data.local.entities.PaymentEntity
import com.example.data.local.entities.PaymentReminderEntity
import com.example.data.local.entities.SettlementEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.local.entities.WorkNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 'primary_profile' LIMIT 1")
    fun getProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 'primary_profile' LIMIT 1")
    suspend fun getProfileSync(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET lastBackupTimestamp = :timestamp WHERE id = 'primary_profile'")
    suspend fun updateLastBackup(timestamp: Long)

    @Query("UPDATE user_profile SET isPinEnabled = :enabled, pinCode = :pin WHERE id = 'primary_profile'")
    suspend fun updatePin(enabled: Boolean, pin: String)

    @Query("UPDATE user_profile SET isBiometricEnabled = :enabled WHERE id = 'primary_profile'")
    suspend fun updateBiometric(enabled: Boolean)

    @Query("UPDATE user_profile SET autoBackupFrequency = :frequency WHERE id = 'primary_profile'")
    suspend fun updateAutoBackup(frequency: String)

    @Query("DELETE FROM user_profile")
    suspend fun deleteProfile()
}

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs ORDER BY createdAt DESC")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs ORDER BY createdAt DESC")
    suspend fun getAllJobsSync(): List<JobEntity>

    @Query("SELECT * FROM jobs WHERE id = :jobId LIMIT 1")
    fun getJobById(jobId: String): Flow<JobEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: JobEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(jobs: List<JobEntity>)

    @Query("DELETE FROM jobs WHERE id = :jobId")
    suspend fun delete(jobId: String)

    @Query("DELETE FROM jobs")
    suspend fun deleteAll()
}

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance WHERE jobId = :jobId ORDER BY dateIso DESC")
    fun getAttendanceForJob(jobId: String): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance ORDER BY dateIso DESC")
    fun getAllAttendance(): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance ORDER BY dateIso DESC")
    suspend fun getAllAttendanceSync(): List<AttendanceEntity>

    @Query("SELECT * FROM attendance WHERE jobId = :jobId AND dateIso BETWEEN :startDate AND :endDate ORDER BY dateIso ASC")
    fun getAttendanceRange(jobId: String, startDate: String, endDate: String): Flow<List<AttendanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendance: AttendanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AttendanceEntity>)

    @Query("DELETE FROM attendance WHERE dateIso = :dateIso AND jobId = :jobId")
    suspend fun delete(dateIso: String, jobId: String)

    @Query("DELETE FROM attendance")
    suspend fun deleteAll()
}

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments WHERE jobId = :jobId ORDER BY dateIso DESC, createdAt DESC")
    fun getPaymentsForJob(jobId: String): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments ORDER BY dateIso DESC, createdAt DESC")
    fun getAllPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments ORDER BY dateIso DESC, createdAt DESC")
    suspend fun getAllPaymentsSync(): List<PaymentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: PaymentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PaymentEntity>)

    @Query("DELETE FROM payments WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM payments")
    suspend fun deleteAll()
}

@Dao
interface SettlementDao {
    @Query("SELECT * FROM settlements WHERE jobId = :jobId AND year = :year AND month = :month LIMIT 1")
    fun getSettlement(jobId: String, year: Int, month: Int): Flow<SettlementEntity?>

    @Query("SELECT * FROM settlements ORDER BY year DESC, month DESC")
    fun getAllSettlements(): Flow<List<SettlementEntity>>

    @Query("SELECT * FROM settlements ORDER BY year DESC, month DESC")
    suspend fun getAllSettlementsSync(): List<SettlementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settlement: SettlementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<SettlementEntity>)

    @Query("DELETE FROM settlements")
    suspend fun deleteAll()
}

@Dao
interface WorkNoteDao {
    @Query("SELECT * FROM work_notes WHERE jobId = :jobId ORDER BY dateIso DESC, createdAt DESC")
    fun getNotesForJob(jobId: String): Flow<List<WorkNoteEntity>>

    @Query("SELECT * FROM work_notes ORDER BY dateIso DESC, createdAt DESC")
    fun getAllNotes(): Flow<List<WorkNoteEntity>>

    @Query("SELECT * FROM work_notes ORDER BY dateIso DESC, createdAt DESC")
    suspend fun getAllNotesSync(): List<WorkNoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: WorkNoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<WorkNoteEntity>)

    @Query("DELETE FROM work_notes WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM work_notes")
    suspend fun deleteAll()
}

@Dao
interface PaymentReminderDao {
    @Query("SELECT * FROM payment_reminders ORDER BY isResolved ASC, dueDateIso ASC")
    fun getAllReminders(): Flow<List<PaymentReminderEntity>>

    @Query("SELECT * FROM payment_reminders ORDER BY isResolved ASC, dueDateIso ASC")
    suspend fun getAllRemindersSync(): List<PaymentReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: PaymentReminderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PaymentReminderEntity>)

    @Query("UPDATE payment_reminders SET isResolved = 1 WHERE id = :id")
    suspend fun markResolved(id: String)

    @Query("DELETE FROM payment_reminders WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM payment_reminders")
    suspend fun deleteAll()
}

@Dao
interface BackupHistoryDao {
    @Query("SELECT * FROM backup_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<BackupHistoryEntity>>

    @Query("SELECT * FROM backup_history ORDER BY timestamp DESC")
    suspend fun getAllHistorySync(): List<BackupHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(backup: BackupHistoryEntity)

    @Query("DELETE FROM backup_history")
    suspend fun deleteAll()
}
