package com.example.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.HisabDatabase
import com.example.data.local.entities.AttendanceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class ReminderActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val db = HisabDatabase.getInstance(context)
        val scope = CoroutineScope(Dispatchers.IO)
        val todayIso = LocalDate.now().toString()

        scope.launch {
            val jobs = db.jobDao().getAllJobsSync()
            val job = jobs.firstOrNull()
            if (job != null) {
                val dailyRate = job.dailyRate
                if (action == "ACTION_MARK_PRESENT") {
                    db.attendanceDao().insert(
                        AttendanceEntity(
                            dateIso = todayIso,
                            jobId = job.id,
                            status = "PRESENT",
                            workingHours = "8h 0m",
                            earnings = dailyRate
                        )
                    )
                } else if (action == "ACTION_MARK_ABSENT") {
                    db.attendanceDao().insert(
                        AttendanceEntity(
                            dateIso = todayIso,
                            jobId = job.id,
                            status = "ABSENT",
                            workingHours = "0h 0m",
                            earnings = 0.0
                        )
                    )
                }
            }
        }
    }
}
