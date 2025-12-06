package com.example.fitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

    private lateinit var summaryManager: TodaySummaryManager

    // UI 요소
    private lateinit var tvWelcome: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTodayWorkouts: TextView
    private lateinit var tvTodayVolume: TextView
    private lateinit var btnStartWorkout: Button
    private lateinit var btnQuickRoutine: Button

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
        tvTodayWorkouts = view.findViewById(R.id.tvTodayWorkouts)
        tvTodayVolume = view.findViewById(R.id.tvTodayVolume)
        btnStartWorkout = view.findViewById(R.id.btnStartWorkout)
        btnQuickRoutine = view.findViewById(R.id.btnQuickRoutine)

        // 현재 날짜 표시
        val dateFormat = SimpleDateFormat("yyyy년 M월 d일 EEEE", Locale.KOREAN)
        tvDate.text = dateFormat.format(Date())

        // 환영 메시지
        tvWelcome.text = getGreetingMessage()
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
        // 운동 시작 버튼
        btnStartWorkout.setOnClickListener {
            // 운동 정보 화면으로 이동 (다른 팀원 담당)
            findNavController().navigate(R.id.action_home_to_exercise_search)
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

    override fun onResume() {
        super.onResume()
        // 화면 돌아올 때마다 새로고침
        loadTodaySummary()
    }
}