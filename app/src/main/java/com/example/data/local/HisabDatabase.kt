package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AttendanceDao
import com.example.data.local.dao.BackupHistoryDao
import com.example.data.local.dao.JobDao
import com.example.data.local.dao.PaymentDao
import com.example.data.local.dao.PaymentReminderDao
import com.example.data.local.dao.SettlementDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.dao.WorkNoteDao
import com.example.data.local.entities.AttendanceEntity
import com.example.data.local.entities.BackupHistoryEntity
import com.example.data.local.entities.JobEntity
import com.example.data.local.entities.PaymentEntity
import com.example.data.local.entities.PaymentReminderEntity
import com.example.data.local.entities.SettlementEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.local.entities.WorkNoteEntity

@Database(
    entities = [
        UserProfileEntity::class,
        JobEntity::class,
        AttendanceEntity::class,
        PaymentEntity::class,
        SettlementEntity::class,
        WorkNoteEntity::class,
        PaymentReminderEntity::class,
        BackupHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HisabDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun jobDao(): JobDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun paymentDao(): PaymentDao
    abstract fun settlementDao(): SettlementDao
    abstract fun workNoteDao(): WorkNoteDao
    abstract fun paymentReminderDao(): PaymentReminderDao
    abstract fun backupHistoryDao(): BackupHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HisabDatabase? = null

        fun getInstance(context: Context): HisabDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HisabDatabase::class.java,
                    "hisab_master_records.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
