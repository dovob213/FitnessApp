package com.example.fitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitness.analytics.*
import com.example.fitness.model.*

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 데이터 준비 (실제로는 DB나 SharedPref에서 불러와야 함)
        val myHistory = getSampleHistory()
        val myPreset = getSamplePreset() // 목표 비비교용

        // 2. 분석 매니저 호출
        val analysis = WorkoutAnalyticsManager.analyze(
            logs = myHistory,
            period = AnalysisPeriod.WEEKLY,
            preset = myPreset
        )

        // 3. UI 업데이트
        updateSummaryUI(view, analysis)
        updateDailyProgressUI(view, analysis.dailyCompletionRate)
        updateMuscleUI(view, analysis.muscleDistribution)
    }

    // 상단 요약 카드 업데이트
    private fun updateSummaryUI(view: View, result: AnalysisResult) {
        // 시간
        val tvTotalTime = view.findViewById<TextView>(R.id.tvTotalTime)
        val pbTime = view.findViewById<ProgressBar>(R.id.progressTime)

        val hours = result.totalTimeMinutes / 60
        val mins = result.totalTimeMinutes % 60
        tvTotalTime.text = "${hours}h ${mins}m"
        pbTime.progress = 70 // 예: 목표 시간 대비 퍼센트 로직 추가 가능

        // 칼로리
        val tvTotalCal = view.findViewById<TextView>(R.id.tvTotalCalorie)
        tvTotalCal.text = String.format("%,d kcal", result.totalCalories)

        // 일수
        val tvTotalDays = view.findViewById<TextView>(R.id.tvTotalDays)
        val pbDays = view.findViewById<ProgressBar>(R.id.progressDays)
        tvTotalDays.text = "${result.workoutDaysCount} / 7 days"
        pbDays.progress = (result.workoutDaysCount / 7.0 * 100).toInt()
    }

    // 요일별 진행률 업데이트
    private fun updateDailyProgressUI(view: View, dailyRates: Map<String, Int>) {
        // 각 요일별 프로그레스바 찾아서 값 세팅
        val monRate = dailyRates["MON"] ?: 0
        view.findViewById<ProgressBar>(R.id.pbMon)?.progress = monRate

        // ... 반복문으로 처리
    }

    // 부위별 분포 업데이트
    private fun updateMuscleUI(view: View, distribution: Map<String, Int>) {
        val upperRate = distribution["상체"] ?: 0
        val lowerRate = distribution["하체"] ?: 0

        view.findViewById<ProgressBar>(R.id.pbUpperBody)?.progress = upperRate
        view.findViewById<TextView>(R.id.tvUpperText)?.text = "${upperRate}%"

        view.findViewById<ProgressBar>(R.id.pbLowerBody)?.progress = lowerRate
        // ...
    }

    // 테스트용 샘플 데이터 생성
    private fun getSampleHistory(): List<WorkoutLog> {
        return listOf(
            WorkoutLog(exerciseId = "ex005", date = System.currentTimeMillis(), sets = listOf()),
            WorkoutLog(exerciseId = "ex007", date = System.currentTimeMillis(), sets = listOf())
        )
    }

    private fun getSamplePreset(): WeeklyPreset? {
        // 빈 프리셋 반환 (실제로는 저장된 프리셋 로드)
        return WeeklyPreset("p01", "My Plan")
    }
}
