package com.example.fitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness.data.RepositoryProvider
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private val workoutRepo by lazy {
        RepositoryProvider.getWorkoutRepository()
    }
    private val exerciseRepo by lazy {
        RepositoryProvider.getExerciseRepository()
    }

    private lateinit var summaryManager: TodaySummaryManager

    // UI 요소
    private lateinit var tvWelcome: TextView
    private lateinit var tvDate: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var tvSelectedDateLabel: TextView
    private lateinit var rvWorkoutHistory: RecyclerView
    private lateinit var tvNoWorkouts: TextView
    private lateinit var tvTodayWorkouts: TextView
    private lateinit var tvTodayVolume: TextView
    private lateinit var btnStartWorkout: Button
    private lateinit var btnQuickRoutine: Button

    private lateinit var workoutHistoryAdapter: WorkoutHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // UI 초기화
        initViews(view)

        // Manager 초기화
        summaryManager = TodaySummaryManager(workoutRepo)

        // 데이터 로드
        loadTodaySummary()

        // 리스너 설정
        setupListeners()

        return view
    }

    private fun initViews(view: View) {
        tvWelcome = view.findViewById(R.id.tvWelcome)
        tvDate = view.findViewById(R.id.tvDate)
        calendarView = view.findViewById(R.id.calendarView)
        tvSelectedDateLabel = view.findViewById(R.id.tvSelectedDateLabel)
        rvWorkoutHistory = view.findViewById(R.id.rvWorkoutHistory)
        tvNoWorkouts = view.findViewById(R.id.tvNoWorkouts)
        tvTodayWorkouts = view.findViewById(R.id.tvTodayWorkouts)
        tvTodayVolume = view.findViewById(R.id.tvTodayVolume)
        btnStartWorkout = view.findViewById(R.id.btnStartWorkout)
        btnQuickRoutine = view.findViewById(R.id.btnQuickRoutine)

        // RecyclerView 설정
        workoutHistoryAdapter = WorkoutHistoryAdapter()
        rvWorkoutHistory.layoutManager = LinearLayoutManager(requireContext())
        rvWorkoutHistory.adapter = workoutHistoryAdapter

        // 현재 날짜 표시
        val dateFormat = SimpleDateFormat("yyyy년 M월 d일 EEEE", Locale.KOREAN)
        tvDate.text = dateFormat.format(Date())

        // 환영 메시지
        tvWelcome.text = getGreetingMessage()

        // 달력 날짜 선택 리스너
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            loadWorkoutsForDate(year, month, dayOfMonth)
        }

        // 오늘 날짜의 운동 기록 로드
        val today = Calendar.getInstance()
        loadWorkoutsForDate(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun loadTodaySummary() {
        lifecycleScope.launch {
            val summary = summaryManager.getTodaySummary()

            // UI 업데이트
            tvTodayWorkouts.text = "오늘 ${summary.workoutCount}회 운동"
            tvTodayVolume.text = String.format("총 볼륨: %.1fkg", summary.totalVolume)
        }
    }

    private fun setupListeners() {
        // 운동 시작 버튼 - 루틴 선택 화면으로 이동
        btnStartWorkout.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_routine_selection)
        }

        // 빠른 루틴 버튼
        btnQuickRoutine.setOnClickListener {
            // 루틴 목록으로 이동 (다른 팀원 담당)
            findNavController().navigate(R.id.action_home_to_routine)
        }
    }

    private fun getGreetingMessage(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "좋은 아침이에요! 💪"
            in 12..17 -> "힘찬 오후 보내세요! 🔥"
            in 18..21 -> "활기찬 저녁이에요! ⚡"
            else -> "늦은 시간까지 수고하셨어요! 🌙"
        }
    }

    private fun loadWorkoutsForDate(year: Int, month: Int, dayOfMonth: Int) {
        lifecycleScope.launch {
            // 선택한 날짜 표시
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val selectedDateFormat = SimpleDateFormat("yyyy년 M월 d일 EEEE", Locale.KOREAN)
            tvDate.text = selectedDateFormat.format(selectedDate.time)
            tvSelectedDateLabel.text = "${selectedDateFormat.format(selectedDate.time)}의 운동"

            // 해당 날짜의 시작과 끝 타임스탬프
            val startOfDay = selectedDate.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val endOfDay = selectedDate.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis

            // 운동 기록 가져오기
            val workoutLogs = workoutRepo.getWorkoutLogsByDateRange(startOfDay, endOfDay)

            if (workoutLogs.isEmpty()) {
                // 운동 기록이 없을 때
                rvWorkoutHistory.visibility = View.GONE
                tvNoWorkouts.visibility = View.VISIBLE
            } else {
                // 운동 기록이 있을 때
                tvNoWorkouts.visibility = View.GONE
                rvWorkoutHistory.visibility = View.VISIBLE

                // 운동 이름과 함께 표시
                val workoutsWithNames = workoutLogs.map { log ->
                    val exercise = exerciseRepo.getExercise(log.exerciseId)
                    log to (exercise?.name ?: "알 수 없는 운동")
                }

                workoutHistoryAdapter.setWorkouts(workoutsWithNames)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 화면 돌아올 때마다 새로고침
        loadTodaySummary()

        // 현재 선택된 날짜의 운동 기록 다시 로드
        val date = Calendar.getInstance()
        date.timeInMillis = calendarView.date
        loadWorkoutsForDate(
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        )
    }
}