package com.example.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.Attendance
import com.example.data.models.AttendanceStatus
import com.example.data.models.BackupHistory
import com.example.data.models.DataAccuracyReport
import com.example.data.models.EmployerBalance
import com.example.data.models.FinancialTimelineItem
import com.example.data.models.Job
import com.example.data.models.MonthlyHisab
import com.example.data.models.Payment
import com.example.data.models.PaymentMethod
import com.example.data.models.PaymentReminder
import com.example.data.models.PaymentType
import com.example.data.models.SalaryType
import com.example.data.models.UserProfile
import com.example.data.models.WorkNote
import com.example.data.repository.BackupValidationResult
import com.example.data.repository.HisabRepository
import com.example.data.repository.ImportMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class AiActionProposal(
    val title: String,
    val description: String,
    val type: String, // "ATTENDANCE" or "PAYMENT"
    val dateIso: String = "",
    val attendanceStatus: AttendanceStatus = AttendanceStatus.PRESENT,
    val amount: Double = 0.0,
    val paymentType: PaymentType = PaymentType.ADVANCE,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val note: String = ""
)

data class AiChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "USER" or "AI"
    val text: String,
    val proposal: AiActionProposal? = null
)

class KaamKhataViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HisabRepository(application.applicationContext)

    val jobs: StateFlow<List<Job>> = repository.jobs.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val selectedJobId: StateFlow<String> = repository.selectedJobId
    val userProfile: StateFlow<UserProfile> = repository.userProfile.stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile())
    val isOnboarded: StateFlow<Boolean> = repository.userProfile.map { it.name.isNotBlank() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _selectedYearMonth = MutableStateFlow(Pair(java.time.LocalDate.now().year, java.time.LocalDate.now().monthValue))
    val selectedYearMonth: StateFlow<Pair<Int, Int>> = _selectedYearMonth.asStateFlow()

    val selectedJob: StateFlow<Job?> = combine(jobs, selectedJobId) { list, id ->
        list.find { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val jobAttendanceList: StateFlow<List<Attendance>> = repository.jobAttendanceList.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    val jobPaymentsList: StateFlow<List<Payment>> = repository.jobPaymentsList.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    val allPaymentsList: StateFlow<List<Payment>> = repository.allPaymentsList.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    val currentMonthHisab: StateFlow<MonthlyHisab?> = repository.currentMonthHisab.stateIn(
        viewModelScope, SharingStarted.Eagerly, null
    )

    val workNotes: StateFlow<List<WorkNote>> = repository.workNotesList.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    val paymentReminders: StateFlow<List<PaymentReminder>> = repository.paymentReminders.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    val backupHistory: StateFlow<List<BackupHistory>> = repository.backupHistory.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    val financialTimeline: StateFlow<List<FinancialTimelineItem>> = repository.financialTimeline.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    val employerBalances: StateFlow<List<EmployerBalance>> = repository.employerBalances.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    // Security PIN Lock & Session unlock state
    private val _isAppUnlocked = MutableStateFlow(false)
    val isAppUnlocked: StateFlow<Boolean> = _isAppUnlocked.asStateFlow()

    // Calculated days without backup for health indicator
    val daysWithoutBackup: StateFlow<Int> = combine(userProfile, backupHistory) { profile, history ->
        val latestTs = if (history.isNotEmpty()) {
            history.maxOf { it.timestamp }
        } else {
            profile.lastBackupTimestamp
        }
        if (latestTs == 0L) {
            14
        } else {
            val days = ((System.currentTimeMillis() - latestTs) / (1000 * 60 * 60 * 24)).toInt()
            days.coerceAtLeast(0)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 14)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<AiChatMessage>>(
        listOf(
            AiChatMessage(
                sender = "AI",
                text = "Hello! 👋\nAsk me anything about your attendance, salary or payments in HISAB."
            )
        )
    )
    val chatMessages: StateFlow<List<AiChatMessage>> = _chatMessages.asStateFlow()

    fun selectJob(jobId: String) {
        repository.selectJob(jobId)
    }

    fun setYearMonth(year: Int, month: Int) {
        _selectedYearMonth.value = Pair(year, month)
    }

    fun markAttendance(
        dateIso: String,
        status: AttendanceStatus,
        workingHours: String = "8h 0m",
        overtimeHours: Double = 0.0,
        notes: String = ""
    ) {
        viewModelScope.launch {
            repository.markAttendance(dateIso, status, overtimeHours, notes)
        }
    }

    fun addPayment(
        amount: Double,
        dateIso: String,
        displayDate: String,
        type: PaymentType,
        method: PaymentMethod,
        notes: String,
        transactionRef: String = ""
    ) {
        viewModelScope.launch {
            repository.addPayment(amount, dateIso, displayDate, type, method, notes, transactionRef)
        }
    }

    fun deletePayment(id: String) {
        viewModelScope.launch {
            repository.deletePayment(id)
        }
    }

    fun addOrUpdateJob(job: Job) {
        viewModelScope.launch {
            repository.addOrUpdateJob(job)
        }
    }

    fun deleteJob(jobId: String) {
        viewModelScope.launch {
            repository.deleteJob(jobId)
        }
    }

    fun saveSettlement(jobId: String, year: Int, month: Int) {
        viewModelScope.launch {
            repository.saveSettlement(jobId, year, month)
        }
    }

    fun updateProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            val cur = userProfile.value
            repository.updateUserProfile(cur.copy(name = name, email = email, phone = phone))
        }
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
        viewModelScope.launch {
            repository.updateUserProfile(
                UserProfile(name = userName, email = email, phone = phone)
            )
            val newJob = Job(
                id = "job_" + UUID.randomUUID().toString().take(8),
                title = jobTitle,
                employerName = employerName,
                location = location,
                salaryType = salaryType,
                dailyRate = wage,
                halfDayRate = wage / 2.0,
                monthlyRate = wage * 26.0
            )
            repository.addOrUpdateJob(newJob)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearSessionAndData()
            _isAppUnlocked.value = false
        }
    }

    fun addWorkNote(dateIso: String, title: String, content: String, category: String) {
        viewModelScope.launch {
            repository.addWorkNote(dateIso, title, content, category)
        }
    }

    fun deleteWorkNote(id: String) {
        viewModelScope.launch {
            repository.deleteWorkNote(id)
        }
    }

    fun addPaymentReminder(employerName: String, amount: Double, dueDateIso: String, notes: String) {
        viewModelScope.launch {
            repository.addPaymentReminder(employerName, amount, dueDateIso, notes)
        }
    }

    fun resolveReminder(id: String) {
        viewModelScope.launch {
            repository.markReminderResolved(id)
        }
    }

    fun unlockWithPin(enteredPin: String): Boolean {
        val targetPin = userProfile.value.pinCode.ifEmpty { "1234" }
        if (enteredPin == targetPin) {
            _isAppUnlocked.value = true
            return true
        }
        return false
    }

    fun unlockWithBiometric() {
        _isAppUnlocked.value = true
    }

    fun lockApp() {
        if (userProfile.value.isPinEnabled) {
            _isAppUnlocked.value = false
        }
    }

    fun setPin(enabled: Boolean, pin: String) {
        viewModelScope.launch {
            repository.updatePin(enabled, pin)
            if (enabled) {
                _isAppUnlocked.value = true
            }
        }
    }

    fun setBiometric(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateBiometric(enabled)
        }
    }

    fun setAutoBackup(frequency: String) {
        viewModelScope.launch {
            repository.updateAutoBackup(frequency)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    suspend fun exportJson(): String {
        return repository.exportAllDataAsJson()
    }

    suspend fun exportAttendanceCsv(): String {
        return repository.exportAttendanceCsv()
    }

    suspend fun exportPaymentsCsv(): String {
        return repository.exportPaymentsCsv()
    }

    fun validateBackup(json: String): BackupValidationResult {
        return repository.validateBackupJson(json)
    }

    suspend fun importBackup(json: String, mode: ImportMode): Boolean {
        return repository.importBackupJson(json, mode)
    }

    suspend fun checkIntegrity(): DataAccuracyReport {
        return repository.checkDataIntegrity()
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }

    fun processAiQuery(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return

        val userMsg = AiChatMessage(sender = "USER", text = trimmed)
        val current = _chatMessages.value.toMutableList()
        current.add(userMsg)

        val lower = trimmed.lowercase()
        val curJob = selectedJob.value

        val response: AiChatMessage = when {
            lower.contains("हाजिरी") || lower.contains("attendance") || lower.contains("present") -> {
                val proposal = AiActionProposal(
                    title = "हाजिरी पुष्टि (Confirm Attendance)",
                    description = "दिनांक: 16 Apr 2025 • Wednesday\nकाम: ${curJob?.title ?: "Site Engineer - ABC Infra"}\nस्थिति: Present (उपस्थित)\nकमाई: ₹${curJob?.dailyRate?.toInt() ?: 600}",
                    type = "ATTENDANCE",
                    dateIso = "2025-04-16",
                    attendanceStatus = AttendanceStatus.PRESENT,
                    note = "Marked via HISAB AI Assistant"
                )
                AiChatMessage(
                    sender = "AI",
                    text = "मैंने 16 Apr 2025 की हाजिरी तैयार की है। जोड़ने के लिए कन्फर्म करें:",
                    proposal = proposal
                )
            }
            lower.contains("सैलरी") || lower.contains("salary") || lower.contains("बनी") -> {
                AiChatMessage(
                    sender = "AI",
                    text = "📊 इस महीने की सैलरी का हिसाब:\n• कुल कमाई (Earned): ₹18,500\n• प्राप्त रकम (Received): ₹12,000\n• शेष बाकी रकम (Pending): ₹6,500\n• कुल उपस्थित दिन: 25 दिन\n(सभी रिकॉर्ड सुरक्षित रूप से आपके फ़ोन में संचित हैं)"
                )
            }
            lower.contains("बाकी") || lower.contains("pending") -> {
                AiChatMessage(
                    sender = "AI",
                    text = "💰 आपका कुल बकाया हिसाब:\n• कुल कमाई: ₹18,500\n• आपको प्राप्त हुआ: ₹12,000\n👉 बाकी लेना है: ₹6,500"
                )
            }
            else -> {
                AiChatMessage(
                    sender = "AI",
                    text = "Hello Rohit! Welcome to HISAB. You can ask me:\n• 'आज मेरी हाजिरी लगा दो'\n• 'इस महीने कितनी सैलरी बनी?'\n• 'मेरा कितना पैसा बाकी है?'"
                )
            }
        }
        current.add(response)
        _chatMessages.value = current
    }

    fun confirmAiAction(proposal: AiActionProposal) {
        if (proposal.type == "ATTENDANCE") {
            markAttendance(
                dateIso = proposal.dateIso,
                status = proposal.attendanceStatus,
                notes = proposal.note
            )
        }
        val current = _chatMessages.value.toMutableList()
        current.add(
            AiChatMessage(
                sender = "AI",
                text = "✅ सफलता! हाजिरी आपके लोकल डेटाबेस में दर्ज कर दी गई है।"
            )
        )
        _chatMessages.value = current
    }
}
