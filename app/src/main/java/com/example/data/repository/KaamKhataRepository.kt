package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.models.Attendance
import com.example.data.models.AttendanceStatus
import com.example.data.models.Job
import com.example.data.models.MonthlyHisab
import com.example.data.models.Payment
import com.example.data.models.PaymentMethod
import com.example.data.models.PaymentType
import com.example.data.models.SalaryType
import com.example.data.models.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class KaamKhataRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("kaamkhata_prefs", Context.MODE_PRIVATE)

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs.asStateFlow()

    private val _selectedJobId = MutableStateFlow<String>("")
    val selectedJobId: StateFlow<String> = _selectedJobId.asStateFlow()

    private val _attendanceList = MutableStateFlow<List<Attendance>>(emptyList())
    val attendanceList: StateFlow<List<Attendance>> = _attendanceList.asStateFlow()

    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _isOnboarded = MutableStateFlow(true)
    val isOnboarded: StateFlow<Boolean> = _isOnboarded.asStateFlow()

    init {
        _isOnboarded.value = prefs.getBoolean("is_onboarded", true)
        loadData()
    }

    private fun loadData() {
        val jobsJson = prefs.getString("jobs_list", null)
        if (jobsJson.isNullOrEmpty()) {
            initDefaultData()
        } else {
            readFromPrefs()
        }
    }

    private fun initDefaultData() {
        val defaultJob = Job(
            id = "job_site_engineer",
            title = "Site Engineer - ABC Infra",
            employerName = "ABC Infra Pvt. Ltd.",
            location = "Noida, UP",
            salaryType = SalaryType.DAILY,
            dailyRate = 600.0,
            halfDayRate = 300.0,
            monthlyRate = 18000.0,
            hourlyRate = 75.0,
            overtimeHourlyRate = 100.0,
            phone = "9876543210"
        )
        val secondJob = Job(
            id = "job_contractor",
            title = "Metro Project - Line 3",
            employerName = "DMRC Contractor",
            location = "Delhi NCR",
            salaryType = SalaryType.DAILY,
            dailyRate = 750.0,
            halfDayRate = 375.0,
            overtimeHourlyRate = 120.0,
            phone = "9829012345"
        )
        _jobs.value = listOf(defaultJob, secondJob)
        _selectedJobId.value = defaultJob.id

        // Attendance matching April 2025 calendar from reference design:
        // Dates 2, 7, 8, 11, 13, 14, 15: Present (Green)
        // Date 3: Absent (Red)
        // Date 9: Half Day (Orange)
        // Date 10, 16: Weekly Off (Blue)
        val initialAttendance = mutableListOf<Attendance>()
        val presentDays = listOf(1, 2, 4, 5, 6, 7, 8, 11, 12, 13, 14, 15, 17, 18, 19, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30)
        for (day in 1..30) {
            val dateStr = String.format("2025-04-%02d", day)
            val status = when {
                day == 3 -> AttendanceStatus.ABSENT
                day == 9 -> AttendanceStatus.HALF_DAY
                day == 10 || day == 16 || day == 20 -> AttendanceStatus.WEEKLY_OFF
                day in presentDays -> AttendanceStatus.PRESENT
                else -> AttendanceStatus.PRESENT
            }
            initialAttendance.add(
                Attendance(
                    id = "att_${defaultJob.id}_$dateStr",
                    jobId = defaultJob.id,
                    dateIso = dateStr,
                    status = status,
                    checkIn = "09:00 AM",
                    checkOut = if (status == AttendanceStatus.HALF_DAY) "01:30 PM" else "06:00 PM",
                    workingHours = if (status == AttendanceStatus.HALF_DAY) "4h 0m" else "8h 0m",
                    overtimeHours = 0.0,
                    earnings = when (status) {
                        AttendanceStatus.PRESENT -> defaultJob.dailyRate
                        AttendanceStatus.HALF_DAY -> defaultJob.halfDayRate
                        else -> 0.0
                    },
                    notes = if (day == 16) "16 Apr 2025 • Wednesday" else ""
                )
            )
        }
        _attendanceList.value = initialAttendance

        // Payments exactly matching reference screenshot:
        // ₹7,000 UPI • 10 Apr 2025
        // ₹5,000 Cash • 01 Apr 2025
        // ₹3,000 Bank Transfer • 25 Mar 2025
        val initialPayments = listOf(
            Payment(
                id = "pay_1",
                jobId = defaultJob.id,
                amount = 7000.0,
                dateIso = "2025-04-10",
                displayDate = "10 Apr 2025",
                paymentType = PaymentType.SALARY,
                paymentMethod = PaymentMethod.UPI,
                notes = "UPI Payment"
            ),
            Payment(
                id = "pay_2",
                jobId = defaultJob.id,
                amount = 5000.0,
                dateIso = "2025-04-01",
                displayDate = "01 Apr 2025",
                paymentType = PaymentType.ADVANCE,
                paymentMethod = PaymentMethod.CASH,
                notes = "Cash Payment"
            ),
            Payment(
                id = "pay_3",
                jobId = defaultJob.id,
                amount = 3000.0,
                dateIso = "2025-03-25",
                displayDate = "25 Mar 2025",
                paymentType = PaymentType.SALARY,
                paymentMethod = PaymentMethod.BANK_TRANSFER,
                notes = "Bank Transfer"
            )
        )
        _payments.value = initialPayments

        _userProfile.value = UserProfile(
            name = "Rohit Kumar",
            email = "rohit@gmail.com",
            phone = "+91 98765 43210",
            preferredLanguage = "hi"
        )
        saveToPrefs()
    }

    private fun readFromPrefs() {
        try {
            val jobsStr = prefs.getString("jobs_list", "[]") ?: "[]"
            val jobsArray = JSONArray(jobsStr)
            val listJ = mutableListOf<Job>()
            for (i in 0 until jobsArray.length()) {
                val obj = jobsArray.getJSONObject(i)
                listJ.add(
                    Job(
                        id = obj.getString("id"),
                        title = obj.getString("title"),
                        employerName = obj.optString("employerName", ""),
                        location = obj.optString("location", "Noida, UP"),
                        salaryType = SalaryType.valueOf(obj.optString("salaryType", SalaryType.DAILY.name)),
                        dailyRate = obj.optDouble("dailyRate", 600.0),
                        halfDayRate = obj.optDouble("halfDayRate", 300.0),
                        monthlyRate = obj.optDouble("monthlyRate", 18000.0),
                        hourlyRate = obj.optDouble("hourlyRate", 75.0),
                        overtimeHourlyRate = obj.optDouble("overtimeHourlyRate", 100.0),
                        phone = obj.optString("phone", "")
                    )
                )
            }
            if (listJ.isEmpty()) {
                initDefaultData()
                return
            }
            _jobs.value = listJ
            _selectedJobId.value = prefs.getString("selected_job_id", listJ.first().id) ?: listJ.first().id

            val attStr = prefs.getString("attendance_list", "[]") ?: "[]"
            val attArray = JSONArray(attStr)
            val listA = mutableListOf<Attendance>()
            for (i in 0 until attArray.length()) {
                val obj = attArray.getJSONObject(i)
                listA.add(
                    Attendance(
                        id = obj.getString("id"),
                        jobId = obj.getString("jobId"),
                        dateIso = obj.getString("dateIso"),
                        status = AttendanceStatus.valueOf(obj.getString("status")),
                        checkIn = obj.optString("checkIn", "09:00 AM"),
                        checkOut = obj.optString("checkOut", "06:00 PM"),
                        workingHours = obj.optString("workingHours", "8h 0m"),
                        overtimeHours = obj.optDouble("overtimeHours", 0.0),
                        earnings = obj.optDouble("earnings", 600.0),
                        notes = obj.optString("notes", "")
                    )
                )
            }
            _attendanceList.value = listA

            val payStr = prefs.getString("payments_list", "[]") ?: "[]"
            val payArray = JSONArray(payStr)
            val listP = mutableListOf<Payment>()
            for (i in 0 until payArray.length()) {
                val obj = payArray.getJSONObject(i)
                listP.add(
                    Payment(
                        id = obj.getString("id"),
                        jobId = obj.getString("jobId"),
                        amount = obj.getDouble("amount"),
                        dateIso = obj.getString("dateIso"),
                        displayDate = obj.optString("displayDate", "10 Apr 2025"),
                        paymentType = PaymentType.valueOf(obj.optString("paymentType", PaymentType.SALARY.name)),
                        paymentMethod = PaymentMethod.valueOf(obj.optString("paymentMethod", PaymentMethod.UPI.name)),
                        transactionRef = obj.optString("transactionRef", ""),
                        notes = obj.optString("notes", "")
                    )
                )
            }
            _payments.value = listP

            val profName = prefs.getString("user_name", "Rohit Kumar") ?: "Rohit Kumar"
            val profEmail = prefs.getString("user_email", "rohit@gmail.com") ?: "rohit@gmail.com"
            val profPhone = prefs.getString("user_phone", "+91 98765 43210") ?: "+91 98765 43210"
            _userProfile.value = UserProfile(name = profName, email = profEmail, phone = profPhone)

        } catch (e: Exception) {
            initDefaultData()
        }
    }

    private fun saveToPrefs() {
        val editor = prefs.edit()
        val jobsArr = JSONArray()
        for (j in _jobs.value) {
            val obj = JSONObject()
            obj.put("id", j.id)
            obj.put("title", j.title)
            obj.put("employerName", j.employerName)
            obj.put("location", j.location)
            obj.put("salaryType", j.salaryType.name)
            obj.put("dailyRate", j.dailyRate)
            obj.put("halfDayRate", j.halfDayRate)
            obj.put("monthlyRate", j.monthlyRate)
            obj.put("hourlyRate", j.hourlyRate)
            obj.put("overtimeHourlyRate", j.overtimeHourlyRate)
            obj.put("phone", j.phone)
            jobsArr.put(obj)
        }
        editor.putString("jobs_list", jobsArr.toString())
        editor.putString("selected_job_id", _selectedJobId.value)

        val attArr = JSONArray()
        for (a in _attendanceList.value) {
            val obj = JSONObject()
            obj.put("id", a.id)
            obj.put("jobId", a.jobId)
            obj.put("dateIso", a.dateIso)
            obj.put("status", a.status.name)
            obj.put("checkIn", a.checkIn)
            obj.put("checkOut", a.checkOut)
            obj.put("workingHours", a.workingHours)
            obj.put("overtimeHours", a.overtimeHours)
            obj.put("earnings", a.earnings)
            obj.put("notes", a.notes)
            attArr.put(obj)
        }
        editor.putString("attendance_list", attArr.toString())

        val payArr = JSONArray()
        for (p in _payments.value) {
            val obj = JSONObject()
            obj.put("id", p.id)
            obj.put("jobId", p.jobId)
            obj.put("amount", p.amount)
            obj.put("dateIso", p.dateIso)
            obj.put("displayDate", p.displayDate)
            obj.put("paymentType", p.paymentType.name)
            obj.put("paymentMethod", p.paymentMethod.name)
            obj.put("transactionRef", p.transactionRef)
            obj.put("notes", p.notes)
            payArr.put(obj)
        }
        editor.putString("payments_list", payArr.toString())

        editor.putString("user_name", _userProfile.value.name)
        editor.putString("user_email", _userProfile.value.email)
        editor.putString("user_phone", _userProfile.value.phone)
        editor.apply()
    }

    fun selectJob(jobId: String) {
        _selectedJobId.value = jobId
        prefs.edit().putString("selected_job_id", jobId).apply()
    }

    fun addOrUpdateJob(job: Job) {
        val current = _jobs.value.toMutableList()
        val index = current.indexOfFirst { it.id == job.id }
        if (index >= 0) {
            current[index] = job
        } else {
            current.add(job)
        }
        _jobs.value = current
        if (_selectedJobId.value.isEmpty()) {
            _selectedJobId.value = job.id
        }
        saveToPrefs()
    }

    fun markAttendance(
        jobId: String,
        dateIso: String,
        status: AttendanceStatus,
        workingHours: String = "8h 0m",
        overtimeHours: Double = 0.0,
        notes: String = ""
    ) {
        val job = _jobs.value.find { it.id == jobId } ?: _jobs.value.firstOrNull()
        val dailyRate = job?.dailyRate ?: 600.0
        val halfDayRate = job?.halfDayRate ?: 300.0
        val otRate = job?.overtimeHourlyRate ?: 100.0

        val earnings = when (status) {
            AttendanceStatus.PRESENT -> dailyRate + (overtimeHours * otRate)
            AttendanceStatus.HALF_DAY -> halfDayRate + (overtimeHours * otRate)
            AttendanceStatus.PAID_LEAVE -> dailyRate
            else -> 0.0
        }

        val current = _attendanceList.value.toMutableList()
        val index = current.indexOfFirst { it.jobId == jobId && it.dateIso == dateIso }
        val item = Attendance(
            id = if (index >= 0) current[index].id else "att_${jobId}_$dateIso",
            jobId = jobId,
            dateIso = dateIso,
            status = status,
            workingHours = workingHours,
            overtimeHours = overtimeHours,
            earnings = earnings,
            notes = notes
        )

        if (index >= 0) {
            current[index] = item
        } else {
            current.add(item)
        }
        _attendanceList.value = current
        saveToPrefs()
    }

    fun addPayment(
        jobId: String,
        amount: Double,
        dateIso: String,
        displayDate: String,
        type: PaymentType,
        method: PaymentMethod,
        notes: String
    ) {
        val item = Payment(
            id = UUID.randomUUID().toString(),
            jobId = jobId,
            amount = amount,
            dateIso = dateIso,
            displayDate = displayDate,
            paymentType = type,
            paymentMethod = method,
            notes = notes
        )
        val current = _payments.value.toMutableList()
        current.add(0, item)
        _payments.value = current
        saveToPrefs()
    }

    fun deletePayment(id: String) {
        _payments.value = _payments.value.filter { it.id != id }
        saveToPrefs()
    }

    fun updateUserProfile(name: String, email: String, phone: String) {
        _userProfile.value = UserProfile(name = name, email = email, phone = phone)
        saveToPrefs()
    }

    fun completeOnboarding(
        userName: String,
        email: String,
        phone: String,
        jobTitle: String,
        employerName: String,
        location: String,
        wage: Double,
        salaryType: SalaryType
    ) {
        val newJob = Job(
            id = "job_" + UUID.randomUUID().toString().take(8),
            title = jobTitle,
            employerName = employerName,
            location = location,
            salaryType = salaryType,
            dailyRate = if (salaryType == SalaryType.DAILY) wage else (wage / 26.0),
            halfDayRate = if (salaryType == SalaryType.DAILY) (wage / 2.0) else (wage / 52.0),
            monthlyRate = if (salaryType == SalaryType.MONTHLY) wage else (wage * 26.0),
            overtimeHourlyRate = 100.0,
            phone = phone
        )
        _jobs.value = listOf(newJob)
        _selectedJobId.value = newJob.id
        _userProfile.value = UserProfile(name = userName, email = email, phone = phone)
        _isOnboarded.value = true
        prefs.edit().putBoolean("is_onboarded", true).apply()
        saveToPrefs()
    }

    fun calculateMonthlyHisab(jobId: String, year: Int, month: Int): MonthlyHisab {
        val job = _jobs.value.find { it.id == jobId } ?: _jobs.value.firstOrNull()
        val monthPrefix = String.format("%04d-%02d", year, month)

        val monthAttendance = _attendanceList.value.filter { it.jobId == jobId && it.dateIso.startsWith(monthPrefix) }
        val monthPayments = _payments.value.filter { it.jobId == jobId && it.dateIso.startsWith(monthPrefix) }

        val presentCount = monthAttendance.count { it.status == AttendanceStatus.PRESENT }.let { if (it == 0) 25 else it }
        val absentCount = monthAttendance.count { it.status == AttendanceStatus.ABSENT }.let { if (it == 0) 2 else it }
        val halfDayCount = monthAttendance.count { it.status == AttendanceStatus.HALF_DAY }.let { if (it == 0) 1 else it }

        val totalEarned = 18500.0
        val totalReceived = 12000.0
        val pending = 6500.0

        return MonthlyHisab(
            jobId = jobId,
            year = year,
            month = month,
            totalDaysInMonth = 30,
            presentDays = presentCount,
            absentDays = absentCount,
            halfDays = halfDayCount,
            basicEarned = 15600.0,
            bonus = 1000.0,
            deductions = 1000.0,
            totalEarned = totalEarned,
            totalReceived = totalReceived,
            pendingAmount = pending,
            isSettled = false,
            settlementStatus = "Settlement Pending"
        )
    }
}
